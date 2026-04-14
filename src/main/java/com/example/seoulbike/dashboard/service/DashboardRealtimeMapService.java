package com.example.seoulbike.dashboard.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.seoulbike.dashboard.dao.IDashboardRepository;
import com.example.seoulbike.dashboard.model.DashboardRealtimeMapResponse;
import com.example.seoulbike.dashboard.model.DashboardStationOccupancy;
import com.example.seoulbike.dashboard.model.SeoulBikeApiResult;
import com.example.seoulbike.dashboard.model.SeoulBikeApiResult.SeoulBikeApiRow;

@Service
public class DashboardRealtimeMapService {

    private static final Logger log = LoggerFactory.getLogger(DashboardRealtimeMapService.class);
    
    private final IDashboardRepository dashboardRepository;
    private final RestTemplate restTemplate;

    @Value("${seoul.api.key}")
    private String seoulApiKey;

    public DashboardRealtimeMapService(IDashboardRepository dashboardRepository, RestTemplateBuilder builder) {
        this.dashboardRepository = dashboardRepository;
        this.restTemplate = builder.build();
    }

    // 1. 마스터 데이터 조회 (성능을 위해 Cache 권장)
    @Cacheable(value = "rentalOfficeRegionCache", key = "#region")
    public Map<String, DashboardStationOccupancy> getStationsByRegionFromDb(String region) {
        List<DashboardStationOccupancy> list = dashboardRepository.selectRentalOfficesByRegion(region);
        return list.stream()
                .collect(Collectors.toMap(DashboardStationOccupancy::getStationId, Function.identity()));
    }

    // 2. 실시간 조인 및 핵심 로직
    public DashboardRealtimeMapResponse getRealtimeMapData(String region) {
        // DB에서 지역구 대여소 목록 가져오기
        Map<String, DashboardStationOccupancy> stationMap = getStationsByRegionFromDb(region);
        
        if (stationMap.isEmpty()) {
            log.warn("[RealtimeMap] {} 지역구에 등록된 대여소가 없습니다.", region);
            return new DashboardRealtimeMapResponse(37.5665, 126.9780, new ArrayList<>(), new ArrayList<>());
        }

        // OpenAPI 파트 - 3개의 페이지를 비동기 호출 (1~1000, 1001~2000, 2001~3000)
        CompletableFuture<List<SeoulBikeApiRow>> page1 = fetchApiPageAsync(1, 1000);
        CompletableFuture<List<SeoulBikeApiRow>> page2 = fetchApiPageAsync(1001, 2000);
        CompletableFuture<List<SeoulBikeApiRow>> page3 = fetchApiPageAsync(2001, 3000);

        List<SeoulBikeApiRow> allRows = new ArrayList<>();
        try {
            CompletableFuture.allOf(page1, page2, page3).join();
            allRows.addAll(page1.get());
            allRows.addAll(page2.get());
            allRows.addAll(page3.get());
        } catch (Exception e) {
            log.error("[RealtimeMap] API 호출 실패", e);
            return new DashboardRealtimeMapResponse(37.5665, 126.9780, new ArrayList<>(), new ArrayList<>()); // 방어: 빈 객체 반환 시청 중심
        }

        // 3. 병합 (Join) - 메모리 내 교집합
        for (SeoulBikeApiRow row : allRows) {
            String id = row.getStationId();
            if (stationMap.containsKey(id)) {
                DashboardStationOccupancy occ = stationMap.get(id);
                try {
                    double rate = Double.parseDouble(row.getShared());
                    int parkingCnt = Integer.parseInt(row.getParkingBikeTotCnt());
                    int rackCnt = Integer.parseInt(row.getRackTotCnt());
                    occ.setOccupancyRate(rate);
                    occ.setOccupancyStatus(rate > 100 ? "OVER" : "UNDER");
                    occ.setParkingBikeTotCnt(parkingCnt);
                    occ.setRackTotCnt(rackCnt);
                } catch (NumberFormatException e) {
                    occ.setOccupancyRate(0.0);
                }
            }
        }

        // 4. 정렬 (거치율 최상위/최하위) - 타이브레이커: stationId
        List<DashboardStationOccupancy> sortedList = new ArrayList<>(stationMap.values());
        sortedList.sort(Comparator.comparingDouble(DashboardStationOccupancy::getOccupancyRate)
                .thenComparing(DashboardStationOccupancy::getStationId));
        
        List<DashboardStationOccupancy> bottom4 = new ArrayList<>();
        List<DashboardStationOccupancy> top4 = new ArrayList<>();

        for (int i = 0; i < Math.min(4, sortedList.size()); i++) {
            bottom4.add(sortedList.get(i));
        }

        // 거치율 내림차순을 위해 리스트를 뒤에서부터 접근
        for (int i = sortedList.size() - 1; i >= Math.max(0, sortedList.size() - 4); i--) {
            top4.add(sortedList.get(i));
        }

        // 5. 중심점 계산
        DashboardRealtimeMapResponse result = new DashboardRealtimeMapResponse();
        result.setBottom4Stations(bottom4);
        result.setTop4Stations(top4);

        calculateAndSetCentroid(result, top4, bottom4);

        return result;
    }

    private void calculateAndSetCentroid(DashboardRealtimeMapResponse response, 
                                       List<DashboardStationOccupancy> top4,
                                       List<DashboardStationOccupancy> bottom4) {
        double sumLat = 0.0;
        double sumLon = 0.0;
        int validCount = 0;

        List<DashboardStationOccupancy> all8 = new ArrayList<>();
        all8.addAll(top4);
        all8.addAll(bottom4);

        for (DashboardStationOccupancy occ : all8) {
            if (occ.getLatitude() > 0 && occ.getLongitude() > 0) {
                sumLat += occ.getLatitude();
                sumLon += occ.getLongitude();
                validCount++;
            }
        }

        if (validCount > 0) {
            response.setCenterLatitude(sumLat / validCount);
            response.setCenterLongitude(sumLon / validCount);
        } else {
            response.setCenterLatitude(37.5665); // 기본 시청 좌표
            response.setCenterLongitude(126.9780);
        }
    }

    // 비동기 단위 호출
    private CompletableFuture<List<SeoulBikeApiRow>> fetchApiPageAsync(int start, int end) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = String.format("http://openapi.seoul.go.kr:8088/%s/json/bikeList/%d/%d/", seoulApiKey, start, end);
                SeoulBikeApiResult apiResult = restTemplate.getForObject(url, SeoulBikeApiResult.class);
                
                if (apiResult != null && apiResult.getRentBikeStatus() != null && apiResult.getRentBikeStatus().getRows() != null) {
                    return apiResult.getRentBikeStatus().getRows();
                }
            } catch (Exception e) {
                log.error("[RealtimeMap] 비동기 호출 에러 start={}, end={}: {}", start, end, e.getMessage());
            }
            return new ArrayList<>();
        });
    }
}

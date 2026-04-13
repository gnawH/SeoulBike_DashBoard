package com.example.seoulbike.dashboard.service;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.seoulbike.dashboard.dao.IDashboardRepository;
import com.example.seoulbike.dashboard.model.DashboardQueryFilter;
import com.example.seoulbike.dashboard.model.DashboardWordCloudItem;

/**
 * DashboardWordCloudService
 *
 * 워드클라우드 데이터 조회 서비스입니다.
 *
 * 역할:
 * 1) 로그인 사용자의 담당구(region)를 필터 조건으로 설정합니다.
 * 2) SEOUL_BIKE ↔ RENT_OFFICE_INFO JOIN 쿼리를 실행합니다.
 * 3) 필터 적용 → JOIN → 집계까지 각 단계별 처리 시간을 로그로 출력합니다.
 */
@Service
public class DashboardWordCloudService {

    private static final Logger log = LoggerFactory.getLogger(DashboardWordCloudService.class);

    private final IDashboardRepository dashboardRepository;

    public DashboardWordCloudService(IDashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    /**
     * 담당구 기준 워드클라우드 데이터 조회
     *
     * @param region 로그인 사용자의 담당 구 (예: "양천구") — AuthResponse.region 값
     * @return 대여소명 + 이용횟수 목록 (이용횟수 내림차순)
     */
    public List<DashboardWordCloudItem> getWordCloudData(String region) {

        // ① 파라미터 설정 단계
        long stepStart = System.currentTimeMillis();

        DashboardQueryFilter filter = new DashboardQueryFilter();
        filter.setRegionName(region);

        long filterSetupMs = System.currentTimeMillis() - stepStart;
        log.info("[WORDCLOUD] ① 필터 설정 완료 | 담당구역: {} | 소요: {}ms", region, filterSetupMs);

        // ② SEOUL_BIKE JOIN RENT_OFFICE_INFO + GROUP BY 집계 (DB 쿼리 실행)
        stepStart = System.currentTimeMillis();

        List<DashboardWordCloudItem> result;
        try {
            result = dashboardRepository.selectWordCloudByRegion(filter);
        } catch (Exception e) {
            log.error("[WORDCLOUD] ② DB 조회 실패 | 담당구역: {} | 원인: {}", region, e.getMessage(), e);
            return Collections.emptyList();
        }

        long queryMs = System.currentTimeMillis() - stepStart;
        log.info("[WORDCLOUD] ② DB 조회 완료 (SEOUL_BIKE JOIN RENT_OFFICE_INFO, GROUP BY 대여소명) | 결과 건수: {}건 | 소요: {}ms",
                result.size(), queryMs);

        // ③ 결과 유효성 확인
        if (result.isEmpty()) {
            log.warn("[WORDCLOUD] ③ 조회 결과 없음 | 담당구역: {} | 1년 전 날짜 데이터가 존재하지 않을 수 있음", region);
        } else {
            // 최다 이용 대여소 상위 3개를 미리보기로 로그 출력
            log.info("[WORDCLOUD] ③ 상위 대여소 미리보기: {}",
                    result.stream()
                          .limit(3)
                          .map(item -> item.getStationName() + "(" + item.getRentCount() + "건)")
                          .reduce((a, b) -> a + ", " + b)
                          .orElse("없음"));
        }

        return result;
    }
}

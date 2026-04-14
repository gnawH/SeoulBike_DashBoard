package com.example.seoulbike.dashboard.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardRealtimeMapResponse {
    
    // 최종 조회된 중심 반경 (유효 위경도들의 평균, 없으면 서울시청 기본 좌표)
    private double centerLatitude = 37.5665;
    private double centerLongitude = 126.9780;

    // 상위 거치율 (회수 대상) 대여소 목록 (최대 4개)
    private List<DashboardStationOccupancy> top4Stations;

    // 하위 거치율 (보충 대상) 대여소 목록 (최대 4개)
    private List<DashboardStationOccupancy> bottom4Stations;
}

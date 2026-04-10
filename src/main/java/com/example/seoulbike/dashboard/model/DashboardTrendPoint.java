package com.example.seoulbike.dashboard.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DashboardTrendPoint
 *
 * 기간별(일/월/분기/반기) 대여 횟수 라인차트의 단일 데이터 포인트를 표현합니다.
 *
 * 역할:
 * 1) x축 라벨(기간 문자열)과 y축 값(대여 횟수)을 1:1로 보관합니다.
 * 2) 프론트에서 Plotly 데이터 배열 생성 시 직렬화가 쉬운 형태를 제공합니다.
 */
@Getter
@Setter
@ToString
public class DashboardTrendPoint {
    /** 차트 x축 라벨 예: 10/18, 2025-10, 2025-Q3 */
    private String periodLabel;

    /** 해당 기간의 대여 횟수 */
    private long rentCount;
}

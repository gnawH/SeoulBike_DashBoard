package com.example.seoulbike.dashboard.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DashboardRegionUsage
 *
 * 지역별 이용 분포(워드클라우드/랭킹) 표현에 사용하는 DTO입니다.
 *
 * 역할:
 * 1) 지역명과 이용량을 전달하여 크기/강조도 기반 시각화에 활용합니다.
 * 2) 순위와 비중 값을 함께 전달해 다양한 UI(태그/테이블/차트)에 재사용합니다.
 */
@Getter
@Setter
@ToString
public class DashboardRegionUsage {
    /** 지역명(예: 강남, 여의도) */
    private String regionName;

    /** 해당 지역 이용 횟수 */
    private long usageCount;

    /** 전체 대비 점유율(%) */
    private double usagePercent;

    /** 이용량 기준 순위 */
    private int rank;
}

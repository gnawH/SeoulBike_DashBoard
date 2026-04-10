package com.example.seoulbike.dashboard.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DashboardSeasonUsage
 *
 * 계절별 이용 분포(도넛 차트) 데이터 전달 DTO입니다.
 *
 * 역할:
 * 1) 계절명/이용량/비율을 전달해 파이·도넛 차트에 직접 매핑합니다.
 * 2) 서버 집계 결과를 프론트가 가공 없이 사용할 수 있게 단순화합니다.
 */
@Getter
@Setter
@ToString
public class DashboardSeasonUsage {
    /** 계절명(봄/여름/가을/겨울) */
    private String seasonName;

    /** 계절별 이용 횟수 */
    private long usageCount;

    /** 계절별 이용 비율(%) */
    private double usagePercent;
}

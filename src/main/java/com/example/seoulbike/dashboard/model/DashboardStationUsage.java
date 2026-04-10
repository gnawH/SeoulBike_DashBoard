package com.example.seoulbike.dashboard.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DashboardStationUsage
 *
 * 대여소별 이용자 수(Top N) 막대 차트용 DTO입니다.
 *
 * 역할:
 * 1) 대여소명과 이용자 수를 전달해 순위형 시각화를 구성합니다.
 * 2) 월별 필터가 적용된 조회 결과를 동일한 형식으로 반환합니다.
 */
@Getter
@Setter
@ToString
public class DashboardStationUsage {
    /** 대여소명 */
    private String stationName;

    /** 이용자 수 */
    private long userCount;

    /** 정렬된 순위 값(선택) */
    private int rank;
}

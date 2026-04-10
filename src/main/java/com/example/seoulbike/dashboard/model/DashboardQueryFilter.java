package com.example.seoulbike.dashboard.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DashboardQueryFilter
 *
 * 대시보드 조회 공통 필터(연도/월/집계 단위)를 전달하는 DTO입니다.
 *
 * 역할:
 * 1) DAO/Mapper 메서드 파라미터를 일관된 객체 형태로 통일합니다.
 * 2) 월별/기간별 조회 조건을 추가할 때 시그니처 변경을 최소화합니다.
 */
@Getter
@Setter
@ToString
public class DashboardQueryFilter {
    /** 조회 기준 연도 예: 2025 */
    private Integer year;

    /** 조회 월(1~12), 전체 조회 시 null 허용 */
    private Integer month;

    /** 집계 단위 예: daily, monthly, quarterly, half-yearly */
    private String periodType;

    /** 상위 n개 제한 값(Top N), 미지정 시 null */
    private Integer limit;

    /** 로그인 사용자 ID(선택값, 감사 로그나 사용자별 확장 조건에 사용) */
    private String userId;

    /** 로그인 사용자의 담당 구(예: 양천구) */
    private String regionName;

    /** 거치율 분류 기준값(기본 100) */
    private Double occupancyThreshold;
}

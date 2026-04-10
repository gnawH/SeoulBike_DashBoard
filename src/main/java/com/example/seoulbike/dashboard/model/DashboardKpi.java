package com.example.seoulbike.dashboard.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DashboardKpi
 *
 * 대시보드 상단 KPI 리본(요약 지표 4개)을 화면에 전달하기 위한 DTO입니다.
 *
 * 역할:
 * 1) 서비스 계층에서 계산/집계한 핵심 지표를 한 객체로 묶어 전달합니다.
 * 2) 컨트롤러와 뷰(Thymeleaf/JS) 사이에서 KPI 데이터 계약을 고정합니다.
 * 3) 수치 단위(ton, km, hour 등)와 설명 문구를 함께 내려 UI 문구 변경 비용을 줄입니다.
 */
@Getter
@Setter
@ToString
public class DashboardKpi {
    /** 2025년 총 탄소 절감량 */
    private double carbonReductionTon;

    /** 탄소 절감량을 나무 식재 개수로 환산한 값 */
    private long treeEquivalentCount;

    /** 총 누적 주행거리 */
    private long totalDistanceKm;

    /** 총 누적 주행거리를 지구 둘레 기준으로 환산한 값 */
    private double earthRoundsEquivalent;

    /** 최고 인기 대여소명 */
    private String topStationName;

    /** 최고 인기 대여소 대여 횟수 */
    private long topStationRentCount;

    /** 누적 이용 시간 */
    private long totalUsageHours;

    /** 전년 대비 이용량 증가율(%) */
    private double yearOverYearGrowthRate;
}

package com.example.seoulbike.dashboard.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DashboardGenderRatio
 *
 * 이용자 성별 비율 카드 렌더링에 필요한 데이터 전달용 DTO입니다.
 *
 * 역할:
 * 1) 남/여 비율(%)을 전달해 숫자와 진행바를 동시에 구성합니다.
 * 2) 필요 시 기타/미상 비율을 확장 필드로 추가할 수 있도록 단순 구조를 유지합니다.
 */
@Getter
@Setter
@ToString
public class DashboardGenderRatio {
    /** 남성 비율(%) */
    private double malePercent;

    /** 여성 비율(%) */
    private double femalePercent;
}

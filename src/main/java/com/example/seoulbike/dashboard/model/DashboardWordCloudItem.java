package com.example.seoulbike.dashboard.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DashboardWordCloudItem
 *
 * 워드클라우드 화면 렌더링용 DTO입니다.
 *
 * 역할:
 * 1) 담당 구 + 1년 전 오늘 날짜 기준으로 대여소별 이용횟수 합계를 담습니다.
 * 2) 대여소명(단어)과 이용횟수(크기)를 WordCloud2.js에 전달하는 용도입니다.
 * 3) Serializable 구현으로 세션 캐싱(새로고침 시 재조회 방지)을 지원합니다.
 */
@Getter
@Setter
@ToString
public class DashboardWordCloudItem implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /** 대여소명 (워드클라우드에서 표시될 단어) */
    private String stationName;

    /** 이용횟수 합계 (워드클라우드에서 글자 크기를 결정) */
    private long rentCount;
}

package com.example.seoulbike.dashboard.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DashboardStationOccupancy
 *
 * 실시간 대여소 거치율 지도 및 회수/보충 리스트에 공통으로 쓰는 DTO입니다.
 *
 * 역할:
 * 1) 지도 마커에 필요한 위치/거치율/상태값을 함께 전달합니다.
 * 2) 회수 필요/보충 필요 Top N 목록 렌더링에 동일 데이터를 재사용합니다.
 */
@Getter
@Setter
@ToString
public class DashboardStationOccupancy {
    /** 대여소 식별자 */
    private String stationId;

    /** 대여소명 */
    private String stationName;

    /** 대여소 소속 구 이름 */
    private String regionName;

    /** 위도 */
    private double latitude;

    /** 경도 */
    private double longitude;

    /** 거치율(%) */
    private double occupancyRate;

    /** 상태 코드: OVER(회수 필요), UNDER(보충 필요), NORMAL */
    private String occupancyStatus;
}

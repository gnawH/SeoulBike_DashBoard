package com.example.seoulbike.dashboard.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.seoulbike.dashboard.model.DashboardGenderRatio;
import com.example.seoulbike.dashboard.model.DashboardKpi;
import com.example.seoulbike.dashboard.model.DashboardQueryFilter;
import com.example.seoulbike.dashboard.model.DashboardRegionUsage;
import com.example.seoulbike.dashboard.model.DashboardSeasonUsage;
import com.example.seoulbike.dashboard.model.DashboardStationOccupancy;
import com.example.seoulbike.dashboard.model.DashboardStationUsage;
import com.example.seoulbike.dashboard.model.DashboardTrendPoint;
import com.example.seoulbike.dashboard.model.DashboardWordCloudItem;

/**
 * IDashboardRepository
 *
 * 대시보드 화면 렌더링에 필요한 조회 전용 DAO 인터페이스입니다.
 *
 * 설계 원칙:
 * 1) 대시보드는 읽기 중심 화면이므로 모든 메서드는 SELECT 성격으로 정의합니다.
 * 2) 섹션 단위(KPI/추이/순위/분포/실시간)로 메서드를 분리해 유지보수성을 높입니다.
 * 3) 기간/월/TopN 같은 동적 조건은 DashboardQueryFilter로 통일합니다.
 */
@Mapper
public interface IDashboardRepository {

    /** 상단 KPI 조회 / 1년 전 오늘의 최고 인기 대여소 제외 */
    DashboardKpi selectDashboardKpi(DashboardQueryFilter filter);
    
    /** 1년 전 오늘의 최고 인기 대여소 조회 */
    DashboardKpi bestRentalOffice(DashboardQueryFilter filter);

    /** 기간별 대여 횟수 추이 조회 */
    List<DashboardTrendPoint> selectUsageTrend(DashboardQueryFilter filter);

    /** 대여소별 이용자 수 Top N 조회 */
    List<DashboardStationUsage> selectTopStationsByUsers(DashboardQueryFilter filter);

    /** 성별 이용 비율 조회 */
    DashboardGenderRatio selectGenderRatio(DashboardQueryFilter filter);

    /** 지역별 이용 분포 조회 */
    List<DashboardRegionUsage> selectRegionUsageDistribution(DashboardQueryFilter filter);

    /** 계절별 이용 분포 조회 */
    List<DashboardSeasonUsage> selectSeasonUsageDistribution(DashboardQueryFilter filter);

    /** 실시간 대여소 거치율 지도 데이터 조회 */
    List<DashboardStationOccupancy> selectRealtimeStationOccupancy(DashboardQueryFilter filter);

    /** 로그인 사용자의 구 기준 거치율 상위 N개 대여소 조회 */
    List<DashboardStationOccupancy> selectTop5StationsByRegion(DashboardQueryFilter filter);

    /** 로그인 사용자의 구 기준 거치율 하위 N개 대여소 조회 */
    List<DashboardStationOccupancy> selectBottom5StationsByRegion(DashboardQueryFilter filter);

    /** CSV 다운로드용: 기준값(기본 100%) 초과 대여소 목록 조회(내림차순) */
    List<DashboardStationOccupancy> selectOverThresholdStationsForCsv(DashboardQueryFilter filter);

    /** CSV 다운로드용: 기준값(기본 100%) 이하 대여소 목록 조회(오름차순) */
    List<DashboardStationOccupancy> selectUnderOrEqualThresholdStationsForCsv(DashboardQueryFilter filter);

    /** 회수 필요 대여소 Top N 조회(거치율 과다) */
    List<DashboardStationOccupancy> selectOverOccupiedStations(DashboardQueryFilter filter);

    /** 보충 필요 대여소 Top N 조회(거치율 부족) */
    List<DashboardStationOccupancy> selectUnderOccupiedStations(DashboardQueryFilter filter);

    /** 담당구 기준 워드클라우드 데이터 조회 */
    List<DashboardWordCloudItem> selectWordCloudByRegion(DashboardQueryFilter filter);
}

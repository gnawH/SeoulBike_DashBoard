package com.example.seoulbike.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.seoulbike.auth.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.seoulbike.dashboard.dao.IDashboardRepository;
import com.example.seoulbike.dashboard.model.DashboardKpi;
import com.example.seoulbike.dashboard.model.DashboardQueryFilter;

@Service
public class DashboardService implements IDashboardService {

	@Autowired
	private IDashboardRepository dashboardRepository;
	
	@Override
	public Map<String, Object> getDashboardData(String userId) {
		
		Map<String, Object> result = new HashMap<>();
		
		// 1. KPI 데이터 조회 (전체 누적)
		DashboardQueryFilter filter = new DashboardQueryFilter();
		DashboardKpi kpi = dashboardRepository.selectDashboardKpi(filter);
		
		// 2. 최고 인기 대여소 정보 조회 및 병합
		DashboardKpi topStation = dashboardRepository.bestRentalOffice(filter);
		if (topStation != null) {
			kpi.setTopStationName(topStation.getTopStationName());
			kpi.setTopStationRentCount(topStation.getTopStationRentCount());
		}
		
		result.put("kpi", kpi);
		result.put("message", "환영합니다");
		result.put("bikeStatus", "대여가능");
		                              
		return result;
	}

}

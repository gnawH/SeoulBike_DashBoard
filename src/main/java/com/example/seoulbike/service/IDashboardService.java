package com.example.seoulbike.service;

import java.util.List;
import java.util.Map;

import com.example.seoulbike.dashboard.model.DashboardTrendPoint;

public interface IDashboardService {
	
	Map<String, Object> getDashboardData(String userId);

	List<DashboardTrendPoint> getUsageTrend(String periodType);

}

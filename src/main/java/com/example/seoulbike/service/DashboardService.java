package com.example.seoulbike.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class DashboardService implements IDashboardService {

	@Override
	public Map<String, Object> getDashboardData(String userId) {
		
		Map<String, Object> result = new HashMap<>();
		
		// DB 연결 후 실제 데이터 넣기
		result.put("userId", userId);
		result.put("message", "환영합니다");
		result.put("bikeStatus", "대여가능");
		
		return result;
	}

}

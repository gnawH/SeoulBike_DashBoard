package com.example.seoulbike.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.seoulbike.auth.model.User;

@Service
public class DashboardService implements IDashboardService {

	
	
	@Override
	public Map<String, Object> getDashboardData(String userId) {
		
		Map<String, Object> result = new HashMap<>();
		
		/**DB 연결 후 실제 데이터 넣기
		       타임리프에 넣을 화면용 이름(ex. <sapn th:text="${data.userId}">
		       선규님께 타임리프에 id 값 어떻게 줄지 논의 필요**/
//		result.put("userid", user.getUserId());
		result.put("message", "환영합니다");
		result.put("bikeStatus", "대여가능");
								/** Mapper interface의 메서드이름으로
								 * 건님과 상의 필요 **/
		                              
		return result;
	}

}

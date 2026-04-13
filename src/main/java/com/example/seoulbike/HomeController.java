package com.example.seoulbike;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import com.example.seoulbike.service.IDashboardService;
import com.example.seoulbike.auth.model.AuthResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class HomeController {
	
	@Autowired
	private IDashboardService dashboardService;

	// 메인 홈 페이지
	@GetMapping("/")
	public String home(HttpSession session, Model model) {
		AuthResponse loginUser = (AuthResponse) session.getAttribute("loginUser");
		
		if (loginUser != null) {
			Map<String, Object> dashboardData = dashboardService.getDashboardData(loginUser.getUserId());
			model.addAllAttributes(dashboardData);
		}
		
		return "index";
	}
}

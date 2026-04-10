package com.example.seoulbike.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.seoulbike.auth.service.IAuthService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {
	
	// 민호님 서비스 확인 후 연결 -> 우선 확인
	@Autowired                                                                   
	IAuthService authService;
	
	//로그인 페이지
	@GetMapping("/login")
	public String loginpage() {
		return "auth/login";
	}
	
	//로그인 처리
	@PostMapping("/login")
	public String login(String userid, String password, HttpSession session, Model model) {
		try {
			// JWT Service 연결
			
			
			return "redirect:/";
		}catch (Exception e) {
			model.addAttribute("message", "LOGIN_FAIL");
			return "auth/login";
		}
	}
	
	// 회원가입 페이지
	@GetMapping("/signup")
	public String signupPage() {
		return "auth/signup";
	}
	
	//회원 가입 처리
	@PostMapping("/signup")
	public String signup(String userid, String name, String password,
							String email, String region, Model model) {
		
		try {
			//Todo: Service 연결 후 구현하기
			//authService.signup(signupDto);
			return "redirect:/login";
		}catch(Exception e){
			model.addAttribute("message", "SIGNUP_FAIL");
			return "auth/signup"; 
		}
	}
	
    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // JWT 쿠키 삭제 (팀원 Service 연결 후)
    	
    	session.invalidate();
        return "redirect:/";
    }
	
	
	
}

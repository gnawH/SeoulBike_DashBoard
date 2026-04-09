package com.example.seoulbike.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {
	
	// 민호님 서비스 확인 후 연결
	//@Autowired
	//IAuthService  AuthService;
	
	//로그인 페이지
	@GetMapping("/login")
	public String loginpage() {
		return "auth/login";
	}
	
	//로그인 처리
	@PostMapping("/login")
	public String login(String userid, String password, Model model) {
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
		return "redirect:/auth/login";
	}
	
    // 로그아웃
    @GetMapping("/logout")
    public String logout() {
        // JWT 쿠키 삭제 (팀원 Service 연결 후)
        return "redirect:/";
    }
	
	
	
}

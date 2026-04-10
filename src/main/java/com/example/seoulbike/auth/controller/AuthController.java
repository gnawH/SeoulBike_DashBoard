package com.example.seoulbike.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.seoulbike.auth.model.AuthResponse;
import com.example.seoulbike.auth.model.Login;
import com.example.seoulbike.auth.model.Signup;
import com.example.seoulbike.auth.service.AuthService;
import com.example.seoulbike.auth.service.IAuthService;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@Controller
public class AuthController {
	

	// 민호님 서비스 확인 후 연결
	@Autowired
	IAuthService AuthService;

	
	//로그인 페이지
	@GetMapping("/login")
	public String loginpage() {
		return "auth/login";
	}
	
	//로그인 처리
	@PostMapping("/login")
	public String login(Login loginDto,HttpSession session,HttpServletResponse response,Model model) {
		try {
			// JWT Service 연결
			AuthResponse result = AuthService.login(loginDto);
			
			//JWT 쿠키 저장
			Cookie cookie = new Cookie("JWT", result.getToken());
			cookie.setHttpOnly(true); //HTTPS 통신에서만 쿠키 전송
			cookie.setSecure(false);
			cookie.setPath("/");
			response.addCookie(cookie);
			
			//세션에 loginUser 저장하기 -> 나중에 index.html 구조 바뀌면 그에 맞춰서 적용
			session.setAttribute("loginUser", result);
			
			return "redirect:/";
		}catch (Exception e) {
			model.addAttribute("message", "LOGIN_FAIL");
			return "auth/login";
		}
	}
	
	// 회원가입 페이지
	@GetMapping("/signup")
	public String signupPage(Model model) {
		model.addAttribute("signupDto", new Signup());
		return "auth/signup";
	}
	
	//회원 가입 처리
	@PostMapping("/signup")
	public String signup(@ModelAttribute("signupDto") Signup signupDto, Model model) {
		
		try {
			//Todo: Service 연결 후 구현하기
			AuthService.signup(signupDto);
			return "redirect:/login";
		}catch(RuntimeException e) {
			model.addAttribute("message", e.getMessage());
            return "auth/signup";
		}
		catch(Exception e){
			model.addAttribute("message", "SIGNUP_FAIL");
			return "auth/signup"; 
		}
	}
	
    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        // JWT 쿠키 삭제 (팀원 Service 연결 후)
    	Cookie cookie = new Cookie("JWT", null);
    	cookie.setHttpOnly(true);   
    	cookie.setSecure(false);
    	cookie.setPath("/");
    	cookie.setMaxAge(0);
    	response.addCookie(cookie);
    	//세션 종료
    	session.invalidate();
    	
        return "redirect:/";
    }
	
	
	
}

package com.example.seoulbike.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String signupPage() {
		return "auth/signup";
	}
	
	//회원 가입 처리
	@PostMapping("/signup")
	public String signup(Signup signupDto, Model model) {
		
		try {
			//Todo: Service 연결 후 구현하기
			AuthService.signup(signupDto);
			return "redirect:/login";
		}catch(RuntimeException e) {
			model.addAttribute("message", e.getMessage());
            return "/signup";
		}
		catch(Exception e){
			model.addAttribute("message", "SIGNUP_FAIL");
			return "/signup"; 
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
    //회원정보 수정 페이지
    @GetMapping("/updateUser")
    public String updateUserPage(HttpSession session, Model model, @RequestParam(name = "status", required = false) 
    String status) {
    	AuthResponse loginUser = (AuthResponse) session.getAttribute("loginUser");
    	
    	if (loginUser == null) {
    		return "redirect:/login";
    	}
    	model.addAttribute("loginUser", loginUser);
    	if (status != null) {
    	    model.addAttribute("status", status);
    	}
    	return "auth/updateUser";
    }
    
    //회원정보 수정 처리
    @PostMapping("/auth/update")
    public String updateUser(Signup signupDto, HttpSession session, Model model) {
    	AuthResponse loginUser = (AuthResponse) session.getAttribute("loginUser");
    	
    	if (loginUser == null) {
    		return "redirect:/login";
    	}
    	try {
    		authService.updateUser(signupDto);
    		return "redirect:/";
    	}catch (RuntimeException e) {
            model.addAttribute("message", e.getMessage());
            return "auth/updateUser";
        }catch (Exception e) {
            model.addAttribute("message", "UPDATE_FAIL");
            return "auth/updateUser";
        }
    }
    
    //회원정보 삭제 처리
    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam("password") String password, HttpSession session, 
    		HttpServletResponse response, Model model) {
    	AuthResponse loginUser = (AuthResponse) session.getAttribute("loginUser");
    	
    	if (loginUser == null) {
    		return "redirect:/login";
    	}
    	try {
    		// 비밀번호 검증 후 삭제
            authService.deleteUser(loginUser.getUserId(), password);
            
            // JWT 쿠키 삭제 
        	Cookie cookie = new Cookie("JWT", null);
        	cookie.setHttpOnly(true);   
        	cookie.setSecure(false);
        	cookie.setPath("/");
        	cookie.setMaxAge(0);
        	response.addCookie(cookie);
        	
        	//세션 종료
        	session.invalidate();
        	return "redirect:/"; 
            
    	}catch (RuntimeException e) {
            model.addAttribute("message", e.getMessage());
            return "auth/updateUser";
        } catch (Exception e) {
            model.addAttribute("message", "DELETE_FAIL");
            return "auth/updateUser";
        }
    }
    
    //비밀번호 검증 -> updateUser.html 자바 스크립트 부분
    @PostMapping("/api/auth/verify-password")
    @ResponseBody
    public Map<String, Boolean> verifyPassword(@RequestParam("password") String password,
            HttpSession session){
    	AuthResponse loginUser = (AuthResponse) session.getAttribute("loginUser");
    	
    	if (loginUser == null) {
    		return Map.of("success", false); // 키 값 (success, false)로 반환
    	}
    	try {
    		authService.verifyPasswordWithSecurity(loginUser.getUserId(), password);
    		return Map.of("success", true);
    	}catch(Exception e) {
    		return Map.of("success", false);
    	}
    }
    
    //비밀번호 재설정
    @PostMapping("/auth/reset-password")
    public String resetPassword(@RequestParam("newPassword") String newPassword, HttpSession session, Model model) {
    	AuthResponse loginUser = (AuthResponse) session.getAttribute("loginUser");
    	
    	if (loginUser == null) {
            return "redirect:/login";
        }
    	try {
    		authService.updatePassword(loginUser.getUserId(), newPassword);
    		return "redirect:/updateUser?status=RESET_SUCCESS";
    	}catch (RuntimeException e) {
            model.addAttribute("message", e.getMessage());
            model.addAttribute("loginUser", loginUser);
            return "auth/updateUser";
    	}catch (Exception e) {
            model.addAttribute("message", "RESET_FAIL");
            model.addAttribute("loginUser", loginUser);
            return "auth/updateUser";
    	}
    }

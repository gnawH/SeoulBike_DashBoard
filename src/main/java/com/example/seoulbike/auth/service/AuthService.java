package com.example.seoulbike.auth.service;

import java.util.Date;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.seoulbike.auth.dao.UserMapper;
import com.example.seoulbike.auth.model.AuthResponseDto;
import com.example.seoulbike.auth.model.LoginDto;
import com.example.seoulbike.auth.model.SignupDto;
import com.example.seoulbike.auth.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class AuthService {
				//Mapper(DAO) 영역에서 만들어야하는데 interface findByUserId(String userId)
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	
	public AuthService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
	    this.userMapper = userMapper;
	    this.passwordEncoder = passwordEncoder;
		
	}
	//회원가입
	public void signup(SignupDto dto) {
		
		//1. 중복 체크
		if (userMapper.findByUserId(dto.getUserId()) !=  null) {
			throw new RuntimeException("이미 존재하는 아이디입니다");
		}
		//2. 비밀번호 암호화
//		String encoded = passwordEncoder.encode(dto.//getPassword());
//		dto.setPassword(encoded);
		
		//3. 저장
//		userMapper.insertUser(dto);
		
	}
	//로그인
	public AuthResponseDto login(LoginDto dto) {
		
		//1. 사용자 조회
//		User user = userMapper.findByUserId(dto.getUserId());
//		if (user == null) {
			throw new RuntimeException("아이디가 없습니다");
//		}
		
		//2. 비밀번호 검증
//		if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) { 
//			throw new RuntimeException("비밀번호 틀림"); 
			
		}
		//3. JWT 발급
//		String token = generateToken(user);
		
//		return new AuthResponseDto(user.getUserId(),
//				token);
		
//	}
	
	//JWT  생성 
	private String generateToken(User user) {
		
		return Jwts.builder()
				.setSubject(user.getUserId())
				.claim("role", user.getRole())	
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
				.signWith(SignatureAlgorithm.HS256, "secretKey")
				.compact();
	}
	
	
}

package com.example.seoulbike.auth.service;

import java.util.Date;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.seoulbike.auth.dao.UserMapper;
import com.example.seoulbike.auth.model.AuthResponse;
import com.example.seoulbike.auth.model.Login;
import com.example.seoulbike.auth.model.Signup;
import com.example.seoulbike.auth.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class AuthService implements IAuthService  {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void signup(Signup dto) {
        if (userMapper.findByUserId(dto.getUserId()) != null) {
            throw new RuntimeException("이미 존재하는 아이디입니다");
        }

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        userMapper.insertUser(dto);
    }

    public AuthResponse login(Login dto) {
        User user = userMapper.findByUserId(dto.getUserId());
        if (user == null) {
            throw new RuntimeException("아이디가 없습니다");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }

        String token = generateToken(user);
        return new AuthResponse(token, "로그인 성공", true);
    }

    private String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUserId())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60))
                .signWith(SignatureAlgorithm.HS256, "secretKey")
                .compact();
    }

	@Override
	public void updateUser(Signup dto) {
		User user = userMapper.findByUserId(dto.getUserId());
		
		if (user == null) {
			throw new RuntimeException("사용자가 존재하지 않습니다");
		}
		//비밀번호 변경시 암호화 
		if (dto.getPassword() != null) {
			dto.setPassword(passwordEncoder.encode(dto.getPassword()));
		}
		userMapper.updateUser(dto);
	}

	@Override
	public void deleteUser(String userId) {
		User user = userMapper.findByUserId(userId);
		
		if (user == null) {
			throw new RuntimeException("사용자가 존재하지 않습니다 ");
		}
		userMapper.deleteUser(userId);
	}
}

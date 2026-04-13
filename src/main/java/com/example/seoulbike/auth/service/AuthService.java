package com.example.seoulbike.auth.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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
public class AuthService implements IAuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // 비밀번호 재확인 검증위해 추가한 코드 생성자
    private final AuthenticationManager authenticationManager;

    @Value("${jwt.secret}")
    private String secretKey;

    public AuthService(UserMapper userMapper, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        // 비밀번호 재확인 검증위해 추가 테스트 생성자
        this.authenticationManager = authenticationManager;
    }

    // 추가한 비밀번호 재확인 검증 메서드
    public void verifyPasswordWithSecurity(String userId, String password) {

    }

    // signup
    @Transactional
    public void signup(Signup dto) {
        if (userMapper.findByUserId(dto.getUserId()) != null) {
            throw new RuntimeException("이미 존재하는 아이디입니다");
        }

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        userMapper.insertUser(dto);
    }

    // login
    public AuthResponse login(Login dto) {
        User user = userMapper.findByUserId(dto.getUserId());
        if (user == null) {
            throw new RuntimeException("아이디가 없습니다");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }

        String token = generateToken(user);
        return new AuthResponse(token, "로그인 성공", true, user.getUserId(),
                user.getName(), user.getRegion(), user.getEmail());
    }

    private String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUserId())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // update
    @Override
    public void updateUser(Signup dto) {
        User user = userMapper.findByUserId(dto.getUserId());

        if (user == null) {
            throw new RuntimeException("사용자가 존재하지 않습니다");
        }
        // 비밀번호 변경시 암호화
        if (dto.getPassword() != null) {
            dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        userMapper.updateUser(dto);
    }

    // delete
    @Override
    public void deleteUser(String userId, String password) {
        User user = userMapper.findByUserId(userId);

        if (user == null) {
            throw new RuntimeException("사용자가 존재하지 않습니다 ");
        }

        // 비밀번호 검증 로직 추가
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }
        userMapper.deleteUser(userId);
    }

    public void updatePassword(String userId, String password) {
        User user = userMapper.findByUserId(userId);

        if (user == null) {
            throw new RuntimeException("사용자가 존재하지 않습니다");
        }
        // 비밀번호 변경시 암호화
        if (password != null) {
            password = passwordEncoder.encode(password);
        }
        String encodedPassword = passwordEncoder.encode(password);

        userMapper.updatePassword(userId, encodedPassword);
    }
}

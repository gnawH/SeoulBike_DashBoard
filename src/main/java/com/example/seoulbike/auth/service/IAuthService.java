package com.example.seoulbike.auth.service;

import com.example.seoulbike.auth.model.AuthResponse;
import com.example.seoulbike.auth.model.Login;
import com.example.seoulbike.auth.model.Signup;

public interface IAuthService {

    AuthResponse login(Login dto);

    void signup(Signup dto); // 회원가입

    void updateUser(Signup dto); // 회원수정

    void deleteUser(String userId, String password); // 회원 탈퇴

    // 회원정보 수정 시 비밀번호 재확인
    void verifyPasswordWithSecurity(String userId, String password);
    
    // 비밀번호 업데이트
    void updatePassword(String userId, String password);
}

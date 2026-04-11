package com.example.seoulbike.auth.service;

import com.example.seoulbike.auth.model.AuthResponse;
import com.example.seoulbike.auth.model.Login;
import com.example.seoulbike.auth.model.Signup;

public interface IAuthService {

    AuthResponse login(Login dto);


    void signup(Signup dto); //회원가입
    
    void updateUser(Signup dto); //회원수정
    
    void deleteUser(String userId, String password); //회원 탈퇴 
}


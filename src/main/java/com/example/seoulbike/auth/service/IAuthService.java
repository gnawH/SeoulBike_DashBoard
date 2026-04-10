package com.example.seoulbike.auth.service;

import com.example.seoulbike.auth.model.AuthResponse;
import com.example.seoulbike.auth.model.Login;
import com.example.seoulbike.auth.model.Signup;

public interface IAuthService {

    AuthResponse login(Login dto);

    void signup(Signup dto);
}
package com.example.seoulbike.auth.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter@Getter@ToString
public class AuthResponse {
    private String token;         // JWT 토큰
    private String message;       // 응답 메시지
    private boolean success;      // 성공 여부
    
    public AuthResponse(String token, String message, boolean success) {
        this.token = token;
        this.message = message;
        this.success = success;
    }
}

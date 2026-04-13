package com.example.seoulbike.auth.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AuthResponse {
    private String token; // JWT 토큰
    private String message; // 응답 메시지
    private boolean success; // 성공 여부
    private String userId; // 아이디
    private String name; // 이름
    private String region; // 담당구역

    public AuthResponse(String token, String message, boolean success,
            String userId, String name, String region) {
        this.token = token;
        this.message = message;
        this.success = success;
        this.userId = userId;
        this.name = name;
        this.region = region;
    }
}

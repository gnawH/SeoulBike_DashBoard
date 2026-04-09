package com.example.seoulbike.auth.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class Member {
    private String userId;
    private String name;
    private String password;
    private String email;
    private String region;
    private String role;

}

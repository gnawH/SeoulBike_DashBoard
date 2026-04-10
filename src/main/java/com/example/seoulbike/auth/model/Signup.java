package com.example.seoulbike.auth.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Signup {
    private String userId;
    private String password;
    private String name;
    private String email;
    private String region;
}

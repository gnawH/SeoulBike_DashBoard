package com.example.seoulbike.auth.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Login {
    private String userId;
    private String password;
}

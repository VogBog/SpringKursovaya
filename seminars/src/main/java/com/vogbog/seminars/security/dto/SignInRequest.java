package com.vogbog.seminars.security.dto;

import lombok.Data;

@Data
public class SignInRequest {
    private String login;
    private String password;
}

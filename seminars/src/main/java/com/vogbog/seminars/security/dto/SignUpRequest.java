package com.vogbog.seminars.security.dto;

import lombok.Data;

@Data
public class SignUpRequest {
    private String username;
    private String login;
    private String password;
}

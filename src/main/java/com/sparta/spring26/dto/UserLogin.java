package com.sparta.spring26.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLogin {
    private String email;
    private String password;
}

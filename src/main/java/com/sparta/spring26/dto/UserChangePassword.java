package com.sparta.spring26.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePassword {
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;
}

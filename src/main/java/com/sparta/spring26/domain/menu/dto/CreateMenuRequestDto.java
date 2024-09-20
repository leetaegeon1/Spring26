package com.sparta.spring26.domain.menu.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMenuRequestDto {
    @NotBlank
    private String name;

    @NotNull
    @Positive
    private Integer price;

    @NotBlank
    private String category;
}

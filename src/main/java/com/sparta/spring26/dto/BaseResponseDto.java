package com.sparta.spring26.dto;

import com.sparta.spring26.enums.BaseResponseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseResponseDto {
    private boolean success;
    private int status;
    private String message;

    public BaseResponseDto(BaseResponseEnum baseResponseEnum) {
        this.success = baseResponseEnum.isSuccess();
        this.status = baseResponseEnum.getStatus();
        this.message = baseResponseEnum.getMessage();
    }
}

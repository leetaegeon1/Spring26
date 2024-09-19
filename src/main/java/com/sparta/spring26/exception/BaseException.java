package com.sparta.spring26.exception;

import com.sparta.spring26.enums.BaseResponseEnum;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    BaseResponseEnum response;
    public BaseException(BaseResponseEnum response) {
        this.response = response;
    }
}

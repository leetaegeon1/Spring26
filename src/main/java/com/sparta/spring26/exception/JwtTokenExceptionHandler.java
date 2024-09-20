package com.sparta.spring26.exception;

import com.sparta.spring26.enums.BaseResponseEnum;

public class JwtTokenExceptionHandler extends BaseException {
    public JwtTokenExceptionHandler(BaseResponseEnum response) {
        super(response);
    }
}

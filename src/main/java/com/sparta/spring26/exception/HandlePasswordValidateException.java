package com.sparta.spring26.exception;

import com.sparta.spring26.enums.BaseResponseEnum;
import lombok.Getter;

@Getter
public class HandlePasswordValidateException extends BaseException {
    public HandlePasswordValidateException(BaseResponseEnum response) {
        super(response);
    }
}

package com.sparta.spring26.exception;

import com.sparta.spring26.enums.BaseResponseEnum;
import lombok.Getter;

@Getter
public class HandleNotFoundException extends BaseException{
    public HandleNotFoundException(BaseResponseEnum response) {
        super(response);
    }
}

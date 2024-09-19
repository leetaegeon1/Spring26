package com.sparta.spring26.exception;

import com.sparta.spring26.enums.BaseResponseEnum;
import lombok.Getter;

@Getter
public class UserJoinIdException extends BaseException {
    public UserJoinIdException(BaseResponseEnum response) {
        super(response);
    }
}

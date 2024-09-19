package com.sparta.spring26.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BaseResponseEnum {
    // 공용
    SUCCESS(true, HttpStatus.OK.value(), "요청에 성공 하였습니다"),
    FAIL(false, HttpStatus.BAD_REQUEST.value(), "요청에 실패 하였습니다"),

    // User
    USER_SAVE_SUCCESS(true, HttpStatus.OK.value(), "회원가입에 성공 하였습니다"),
    USER_SAVE_FAIL(false, HttpStatus.BAD_REQUEST.value(), "회원가입에 실패 하였습니다"),
    USER_DELETE_SUCCESS(true, HttpStatus.OK.value(), "회원 탈퇴에 성공 하였습니다"),
    USER_DELETE_FAIL(true, HttpStatus.OK.value(), "회원 탈퇴에 실패 하였습니다"),
    USER_LOGIN_SUCCESS(true, HttpStatus.OK.value(), "로그인에 성공 하였습니다"),
    USER_PASSWORD_CHANGE_SUCCESS(true,HttpStatus.OK.value(), "비밀번호 변경에 성공 하였습니다"),
    USER_NOT_FOUND(false, HttpStatus.NOT_FOUND.value(), "요청 하는 유저가 존재하지 않습니다"),
    USER_NOT_USERNAME(false, HttpStatus.NOT_FOUND.value(), "요청하는 닉네임이 존재하지 않습니다."),
    USER_NOT_EMAIL(false, HttpStatus.NOT_FOUND.value(), "요청하는 이메일이 존재하지 않습니다."),
    USER_INVALID_CREDENTIALS(false, HttpStatus.UNAUTHORIZED.value(), "아이디 또는 비밀번호가 틀렸습니다"),
    USER_INVALID_PASSWORD(false, HttpStatus.UNAUTHORIZED.value(), "비밀번호를 다시 입력해주세요."),
    USER_MISMATCH_BOARD(false, HttpStatus.BAD_REQUEST.value(), "사용자가 요청한 게시글은, 본인이 작성한 게시글이 아닙니다"),
    USER_DUPLICATED(false, HttpStatus.CONFLICT.value(), "중복된 이메일 입니다"),
    USER_USERNAME_DUPLICATED(false, HttpStatus.CONFLICT.value(), "중복된 닉네임 입니다"),
    USER_PASSWORD_FORMAT(false, HttpStatus.BAD_REQUEST.value(), "비밀번호는 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함 된 8~20자의 비밀번호여야 합니다."),
    USER_DELETE_EMAIL(false, HttpStatus.BAD_REQUEST.value(),"탈퇴한 사용자의 아이디는 재사용할 수 없습니다."),
    USER_PASSWORD_CHANGE_FAIL(false,HttpStatus.BAD_REQUEST.value(),"비밀번호 변경에 실패 하였습니다"),


    // Jwt
    JWT_NOT_FOUND(false, HttpStatus.NOT_FOUND.value(), "Jwt가 존재하지 않습니다"),
    JWT_EXPIRED(false, HttpStatus.UNAUTHORIZED.value(), "Jwt가 만료되었습니다"),
    JWT_MALFORMED(false, HttpStatus.UNAUTHORIZED.value(), "Jwt가 손상 되었습니다"),
    JWT_UNSUPPORTED(false, HttpStatus.UNAUTHORIZED.value(), "지원되지 않는 Jwt 입니다"),
    JWT_SIGNATURE_FAIL(false, HttpStatus.UNAUTHORIZED.value(), "시그니처 검증에 실패한 Jwt 입니다"),
    JWT_NOT_VALID(false, HttpStatus.UNAUTHORIZED.value(), "Jwt가 유효하지 않습니다"),

    // Auth
    NOT_ADMIN(false, HttpStatus.FORBIDDEN.value(), "관리자만 접속 가능 합니다"),

    // Password validate
    PASSWORD_MISMATCH(false, HttpStatus.BAD_REQUEST.value(), "부합하지않는 비밀번호입니다"),

    DUPLICATE_NEW_PASSWORD(false, HttpStatus.BAD_REQUEST.value(), "새 비밀번호와 현재 비밀번호가 동일합니다"),

    PASSWORD_FORMAT_NOT_VALID(false, HttpStatus.BAD_REQUEST.value(), "새 비밀번호 형식이 올바르지 않습니다");


    private final boolean success;
    private final int status;
    private final String message;

    BaseResponseEnum(boolean success, int status, String message) {
        this.success = success;
        this.status = status;
        this.message = message;
    }
}

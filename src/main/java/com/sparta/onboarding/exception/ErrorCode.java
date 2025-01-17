package com.sparta.onboarding.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    DUPLICATE_USER(HttpStatus.CONFLICT,"이미 존재하는 사용자입니다"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다."),
    TOKEN_EXPIRATION(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다. 재로그인 해주세요."),
    NOT_SUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰입니다."),
    FALSE_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 JWT 토큰입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}

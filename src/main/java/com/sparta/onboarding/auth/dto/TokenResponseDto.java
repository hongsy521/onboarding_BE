package com.sparta.onboarding.auth.dto;

import lombok.Getter;

@Getter
public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;

}

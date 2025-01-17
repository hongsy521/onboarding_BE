package com.sparta.onboarding.dto;

import lombok.Getter;

@Getter
public class SignupResponseDto {
    private String username;
    private String nickname;
    private AuthorityDto authorities;
}

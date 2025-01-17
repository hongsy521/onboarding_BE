package com.sparta.onboarding.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignupRequestDto {
    @NotBlank(message = "username을 입력해주세요.")
    private String username;
    @NotBlank(message = "password를 입력해주세요.")
    private String password;
    @NotBlank(message = "nickname을 입력해주세요.")
    private String nickname;

}

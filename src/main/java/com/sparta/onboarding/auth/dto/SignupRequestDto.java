package com.sparta.onboarding.auth.dto;

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

    public SignupRequestDto(String username, String password, String nickname) {
        this.username=username;
        this.password=password;
        this.nickname=nickname;
    }
}

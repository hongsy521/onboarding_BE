package com.sparta.onboarding.auth.dto;

import com.sparta.onboarding.auth.model.User;
import lombok.Getter;

@Getter
public class SignupResponseDto {
    private String username;
    private String nickname;
    private AuthorityDto authorities;

    public SignupResponseDto(User user) {
        this.username=user.getUsername();
        this.nickname=user.getNickname();
        this.authorities=new AuthorityDto(user.getAuthorityName());
    }
}

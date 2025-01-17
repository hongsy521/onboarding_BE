package com.sparta.onboarding.auth.dto;

import com.sparta.onboarding.auth.model.RoleEnum;
import lombok.Getter;

@Getter
public class AuthorityDto {
    private RoleEnum authorityName;

    public AuthorityDto(RoleEnum authorityName) {
        this.authorityName=authorityName;
    }
}

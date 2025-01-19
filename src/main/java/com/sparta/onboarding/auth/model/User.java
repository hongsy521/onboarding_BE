package com.sparta.onboarding.auth.model;

import com.sparta.onboarding.auth.dto.SignupRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority_name")
    private RoleEnum authorityName;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Builder
    public User(String username, String password, String nickname, RoleEnum authorityName) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.authorityName = authorityName;
    }

    public void updateRefresh(String refreshToken){
        this.refreshToken=refreshToken;
    }
}

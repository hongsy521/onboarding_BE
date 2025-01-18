package com.sparta.onboarding.auth.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sparta.onboarding.auth.dto.TokenResponseDto;
import com.sparta.onboarding.auth.model.RoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;


@ExtendWith(MockitoExtension.class)
class JwtUtilTest {
    @InjectMocks
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtUtil, "ACCESS_TOKEN_TIME", 600000L);
        ReflectionTestUtils.setField(jwtUtil, "REFRESH_TOKEN_TIME", 604800000L);
        ReflectionTestUtils.setField(jwtUtil, "secretKey", "7ZWc64us7J247YS07ZWY6rOg7Iu27Iq164uI64uk6ryt7Iuc7Lyc7KO87IS47JqU");
        jwtUtil.init();
    }

    @DisplayName("토큰 생성 과정 테스트")
    @Test
    void testCreateToken() {
        // given
        String username = "testUser";
        RoleEnum role = RoleEnum.ROLE_USER;

        // when
        TokenResponseDto tokenResponse = jwtUtil.createToken(username, role);

        // then
        assertNotNull(tokenResponse.getAccessToken());
        assertNotNull(tokenResponse.getRefreshToken());

        assertTrue(tokenResponse.getAccessToken().startsWith(JwtUtil.BEARER_PREFIX));
        assertTrue(tokenResponse.getRefreshToken().startsWith(JwtUtil.BEARER_PREFIX));

        String accessToken = tokenResponse.getAccessToken().replace(JwtUtil.BEARER_PREFIX, "");
        Claims accessClaims = Jwts.parserBuilder()
            .setSigningKey(jwtUtil.getTestKey())
            .build()
            .parseClaimsJws(accessToken)
            .getBody();

        assertEquals(username, accessClaims.getSubject());
        assertEquals(role.toString(), accessClaims.get(JwtUtil.AUTHORIZATION_KEY));
    }
}
package com.sparta.onboarding.auth.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sparta.onboarding.auth.dto.TokenResponseDto;
import com.sparta.onboarding.auth.model.RoleEnum;
import com.sparta.onboarding.exception.CustomException;
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
        ReflectionTestUtils.setField(jwtUtil, "secretKey",
            "7ZWc64us7J247YS07ZWY6rOg7Iu27Iq164uI64uk6ryt7Iuc7Lyc7KO87IS47JqU");
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

    @DisplayName("정상 토큰 검증 테스트")
    @Test
    void testValidToken() {
        // given
        String username = "testUser";
        RoleEnum role = RoleEnum.ROLE_USER;
        TokenResponseDto tokenResponse = jwtUtil.createToken(username, role);
        String validToken = tokenResponse.getAccessToken().replace(JwtUtil.BEARER_PREFIX, "");

        // when
        boolean isValid = jwtUtil.validateToken(validToken);

        // then
        assertTrue(isValid);
    }

    @DisplayName("유효하지 않은 서명에 대한 토큰 검증 테스트")
    @Test
    void testInvalidSignatureToken() {
        // given
        String invalidToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0VXNlciIsImF1dGgiOiJVU0VSIiwiZXhwIjoxNjg0NjU1MjAwLCJpYXQiOjE2ODQ2NTUyMDB9.invalidSignature";

        // when & then
        CustomException exception = assertThrows(CustomException.class,
            () -> jwtUtil.validateToken(invalidToken));
        assertEquals("유효하지 않은 JWT 토큰입니다.", exception.getMessage());
    }

    @DisplayName("지원되지 않는 형식의 토큰 검증 테스트")
    @Test
    void testUnsupportedToken() {
        // given
        String unsupportedToken = "unsupported.token.invalid";

        // when & then
        CustomException exception = assertThrows(CustomException.class,
            () -> jwtUtil.validateToken(unsupportedToken));
        assertEquals("유효하지 않은 JWT 토큰입니다.", exception.getMessage());
    }

    @DisplayName("잘못된 형식의 토큰 검증 테스트")
    @Test
    void testMalformedToken() {
        // given
        String malformedToken = "malformedToken";

        // when & then
        CustomException exception = assertThrows(CustomException.class,
            () -> jwtUtil.validateToken(malformedToken));
        assertEquals("유효하지 않은 JWT 토큰입니다.", exception.getMessage());
    }

    @DisplayName("만료 토큰 검증 테스트")
    @Test
    void testExpiredToken() {
        // given
        ReflectionTestUtils.setField(jwtUtil, "ACCESS_TOKEN_TIME", -1L);
        jwtUtil.init();
        String username = "testUser";
        RoleEnum role = RoleEnum.ROLE_USER;
        TokenResponseDto tokenResponse = jwtUtil.createToken(username, role);
        String expiredToken = tokenResponse.getAccessToken().replace(JwtUtil.BEARER_PREFIX, "");

        // when & then
        CustomException exception = assertThrows(CustomException.class,
            () -> jwtUtil.validateToken(expiredToken));
        assertEquals("만료된 토큰입니다. 재로그인 해주세요.", exception.getMessage());
    }
}
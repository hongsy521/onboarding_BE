package com.sparta.onboarding.auth.security;

import com.sparta.onboarding.auth.jwt.JwtUtil;
import com.sparta.onboarding.auth.model.RoleEnum;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(
        HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        log.info("로그인 사용자 액세스토큰 : {}", req.getHeader(JwtUtil.AUTHORIZATION_HEADER));

        String tokenValue = jwtUtil.getTokenFromRequest(req);
        log.info("추출된 토큰 : {}", tokenValue);

        if (StringUtils.hasText(tokenValue)) {
            tokenValue = jwtUtil.substringToken(tokenValue);

            if (!jwtUtil.validateToken(tokenValue)) {
                log.error("토큰 유효하지 않음");
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            try {
                Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
                String username = info.getSubject();
                RoleEnum role = jwtUtil.getRoleFromToken(tokenValue);

                setAuthentication(username, role);
                log.info("인증된 사용자 : {}", username);
            } catch (Exception e) {
                log.error("토큰으로부터 사용자 정보 추출 에러: {}", e.getMessage());
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        filterChain.doFilter(req, res);
    }

    public void setAuthentication(String username, RoleEnum role) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username, role);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String username, RoleEnum role) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Collection<? extends GrantedAuthority> authorities = getAuthorities(role);
        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(RoleEnum role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

}

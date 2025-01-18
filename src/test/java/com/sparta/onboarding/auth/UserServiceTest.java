package com.sparta.onboarding.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.sparta.onboarding.auth.dto.SignupRequestDto;
import com.sparta.onboarding.auth.dto.SignupResponseDto;
import com.sparta.onboarding.auth.model.User;
import com.sparta.onboarding.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @DisplayName("회원가입 과정 테스트")
    @Test
    void testSignUp() {
        // given
        SignupRequestDto signupRequestDto = new SignupRequestDto("newUser", "newPassword", "newNickname");

        // when
        SignupResponseDto responseDto = userService.signup(signupRequestDto);

        // then
        assertNotNull(responseDto);
        assertEquals("newUser", responseDto.getUsername());
    }

    @DisplayName("중복 username 테스트")
    @Test
    void testDuplicateUsername(){
        // given
        String username = "existingUser";
        String password = "password";
        String nickname = "nickname";
        SignupRequestDto signupRequestDto = new SignupRequestDto(username, password, nickname);

        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.of(new User()));

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.signup(signupRequestDto);
        });

        assertEquals("이미 존재하는 사용자입니다", exception.getMessage());
    }

}
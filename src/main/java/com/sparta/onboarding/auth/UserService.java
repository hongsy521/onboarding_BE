package com.sparta.onboarding.auth;

import com.sparta.onboarding.auth.dto.SignupRequestDto;
import com.sparta.onboarding.auth.dto.SignupResponseDto;
import com.sparta.onboarding.auth.model.User;
import com.sparta.onboarding.exception.CustomException;
import com.sparta.onboarding.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {
        userRepository.findByUsername(signupRequestDto.getUsername()).ifPresent(
            user -> {throw new CustomException(ErrorCode.DUPLICATE_USER);}
        );

        User user = new User(signupRequestDto);
        userRepository.save(user);
        return new SignupResponseDto(user);
    }
}

package com.sparta.onboarding.auth;

import com.sparta.onboarding.auth.dto.SignupRequestDto;
import com.sparta.onboarding.auth.dto.SignupResponseDto;
import com.sparta.onboarding.common.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<SignupResponseDto>> signup(@Valid @RequestBody SignupRequestDto signupRequestDto){
        SignupResponseDto responseDto = userService.signup(signupRequestDto);
        CommonResponse response = new CommonResponse("회원가입이 완료되었습니다.",201,responseDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}

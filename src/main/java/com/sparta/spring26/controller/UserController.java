package com.sparta.spring26.controller;

import com.sparta.spring26.domain.user.entity.User;
import com.sparta.spring26.dto.UserChangePassword;
import com.sparta.spring26.dto.UserLogin;
import com.sparta.spring26.dto.UserRequestDto;
import com.sparta.spring26.dto.UserResponse;
import com.sparta.spring26.enums.BaseResponseEnum;
import com.sparta.spring26.security.UserDetailsImpl;
import com.sparta.spring26.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(("/api/users"))
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<BaseResponseEnum> registerUser(@Valid @RequestBody UserRequestDto requestDto, BindingResult bindingResult) {
        userService.registerUser(requestDto, bindingResult);
        return ResponseEntity.ok(BaseResponseEnum.USER_SAVE_SUCCESS);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<BaseResponseEnum> login(@RequestBody UserLogin userLogin) {
        BaseResponseEnum userResponse = userService.login(userLogin.getEmail(), userLogin.getPassword());
        return ResponseEntity.ok(userResponse);
    }

    // 사용자 정보 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }


    //비밀번호 변경
    @PutMapping("/change-password")
    public ResponseEntity<BaseResponseEnum> changePassword(
                                                           @RequestBody UserChangePassword userChangePassword,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User loginUser = userDetails.getUser();
        BaseResponseEnum response = userService.changePassword(loginUser.getId(), userChangePassword, loginUser);
        return ResponseEntity.ok(response);
    }


    // 회원 탈퇴
    @DeleteMapping("/{userId}")
    public ResponseEntity<BaseResponseEnum> deleteUser(@RequestParam String password, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User loginUser = userDetails.getUser();
        userService.deleteUser(loginUser.getId(), password);
        return ResponseEntity.ok(BaseResponseEnum.USER_DELETE_SUCCESS);
    }
}

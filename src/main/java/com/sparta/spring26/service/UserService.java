package com.sparta.spring26.service;

import com.sparta.spring26.domain.user.entity.User;
import com.sparta.spring26.domain.user.entity.UserRole;
import com.sparta.spring26.dto.UserChangePassword;
import com.sparta.spring26.dto.UserRequestDto;
import com.sparta.spring26.dto.UserResponse;
import com.sparta.spring26.enums.BaseResponseEnum;
import com.sparta.spring26.exception.HandlePasswordValidateException;
import com.sparta.spring26.exception.InvalidRequestException;
import com.sparta.spring26.exception.UserJoinIdException;
import com.sparta.spring26.global.config.PasswordEncoder;
import com.sparta.spring26.repository.UserRepository;
import com.sparta.spring26.util.UtilFind;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UtilFind utilFind;

    @Transactional
    public void registerUser(UserRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();

            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            throw new UserJoinIdException(BaseResponseEnum.USER_PASSWORD_FORMAT);
        }

        if (!isValidEmail(requestDto.getEmail())) {
            throw new UserJoinIdException(BaseResponseEnum.USER_SAVE_FAIL);
        }

        boolean userByEmail = utilFind.userDuplicatedEmail(requestDto.getEmail());
        if (userByEmail) {
            throw new UserJoinIdException(BaseResponseEnum.USER_DUPLICATED);
        }

        UserRole role = requestDto.getRole() != null ? requestDto.getRole() : UserRole.USER;
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        User newUser = new User(requestDto.getEmail(), encodedPassword, role);

        userRepository.save(newUser);

    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    public BaseResponseEnum login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidRequestException("User not found"));

        if (user.isDeleted()) {
            throw new InvalidRequestException("이미 탈퇴한 사용자입니다.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidRequestException("비밀번호가 일치하지 않습니다.");
        }

        return BaseResponseEnum.USER_LOGIN_SUCCESS;
    }

    public UserResponse getUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new InvalidRequestException("사용자를 찾을수 없습니다"));
        // 이미 탈퇴한 사용자 확인
        if (user.isDeleted()) {
            throw new InvalidRequestException("이미 탈퇴한 사용자입니다.");
        }
        return new UserResponse(user.getId(), user.getEmail());
    }

    @Transactional
    public BaseResponseEnum changePassword(long userId, UserChangePassword userChangePassword, User loginUser) {

        if (!passwordEncoder.matches(userChangePassword.getOldPassword(), loginUser.getPassword())) {
            throw new HandlePasswordValidateException(BaseResponseEnum.PASSWORD_MISMATCH);
        }

        if (passwordEncoder.matches(userChangePassword.getNewPassword(), loginUser.getPassword())) {
            throw new HandlePasswordValidateException(BaseResponseEnum.DUPLICATE_NEW_PASSWORD);
        }

        if (!validateNewPassword(userChangePassword.getNewPassword())) {
            throw new HandlePasswordValidateException(BaseResponseEnum.PASSWORD_FORMAT_NOT_VALID);
        }

        String encodedPassword = passwordEncoder.encode(userChangePassword.getNewPassword());
        loginUser.changePassword(encodedPassword);
        return BaseResponseEnum.USER_PASSWORD_CHANGE_SUCCESS;
    }

    @Transactional
    public void deleteUser(long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidRequestException("사용자 정보를 찾을수 없습니다"));

        if (user.isDeleted()) {
            throw new InvalidRequestException("이미 탈퇴한 사용자입니다.");
        }


        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidRequestException("비밀번호가 일치하지 않습니다.");
        }
        user.deleteUser();
    }

    private boolean validateNewPassword(String newPassword) {
        if (newPassword.length() < 8 ||
                !newPassword.matches(".*\\d.*") ||
                !newPassword.matches(".*[A-Z].*") ||
                !newPassword.matches(".*[a-z].*") ||
                !newPassword.matches(".*[!@#$%^&*()_+].*")) {
            throw new InvalidRequestException("새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자 특수문자를 포함해야 합니다.");
        }
        return true;
    }
}

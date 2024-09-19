package com.sparta.spring26.util;

import com.sparta.spring26.domain.user.entity.User;
import com.sparta.spring26.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UtilFind {
    private final UserRepository userRepository;


    public boolean userDuplicatedEmail(String email) {
        Optional<User> userByEmail = userRepository.findByEmail(email);
        if (userByEmail.isPresent()) {
            return true;
        } else {
            return false;
        }
    }
}

package com.hk.backendweb02.user.service;

import com.hk.backendweb02.user.domain.User;
import com.hk.backendweb02.user.dto.UserDto;
import com.hk.backendweb02.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User signUp(UserDto.SignUp requestDto) {
        if (userRepository.findByUserId(requestDto.getUserId()).isPresent()) {
            throw new IllegalArgumentException("중복된 아이디가 있습니다.");
        }
        User user = new User();
        user.setUserId(requestDto.getUserId());
        user.setPassword(requestDto.getPassword());
        user.setName(requestDto.getName());
        user.setPhone(requestDto.getPhone());
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User login(UserDto.Login requestDto) {
        User user = userRepository.findByUserId(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("회원정보가 일치하지 않습니다."));
        if (!user.getPassword().equals(requestDto.getPassword())) {
            throw new IllegalArgumentException("회원정보가 일치하지 않습니다.");
        }
        return user;
    }
}
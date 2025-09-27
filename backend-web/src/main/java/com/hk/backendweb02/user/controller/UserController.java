package com.hk.backendweb02.user.controller;

import com.hk.backendweb02.user.domain.User;
import com.hk.backendweb02.user.dto.UserDto;
import com.hk.backendweb02.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody UserDto.SignUp requestDto) {
        try {
            userService.signUp(requestDto);
            return ResponseEntity.ok("회원가입 성공!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto.Login requestDto) {
        try {
            User user = userService.login(requestDto);
            UserDto.Response responseDto = new UserDto.Response();
            responseDto.setId(user.getId());
            responseDto.setName(user.getName());
            return ResponseEntity.ok(responseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
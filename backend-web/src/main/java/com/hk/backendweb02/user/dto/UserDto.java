package com.hk.backendweb02.user.dto;

import lombok.Getter;
import lombok.Setter;

public class UserDto {
    @Getter
    @Setter
    public static class SignUp {
        private String userId;
        private String password;
        private String name;
        private String phone;
    }

    @Getter
    @Setter
    public static class Login {
        private String userId;
        private String password;
    }

    @Getter
    @Setter
    public static class Response {
        private Long id;
        private String name;
    }
}
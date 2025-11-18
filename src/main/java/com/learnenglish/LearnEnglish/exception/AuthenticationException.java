package com.learnenglish.LearnEnglish.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends AppException {
    public AuthenticationException(String message) {
        super("AUTH_ERROR", message, HttpStatus.UNAUTHORIZED);
    }
}
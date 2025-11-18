package com.learnenglish.LearnEnglish.exception;

import org.springframework.http.HttpStatus;

public class AuthorizationException extends AppException {
    public AuthorizationException(String message) {
        super("FORBIDDEN", message, HttpStatus.FORBIDDEN);
    }
}

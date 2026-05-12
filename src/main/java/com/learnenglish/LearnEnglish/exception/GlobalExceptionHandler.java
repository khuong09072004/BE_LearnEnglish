package com.learnenglish.LearnEnglish.exception;

import com.learnenglish.LearnEnglish.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<ApiResponse<Object>> handleAuthenticationExceptions(RuntimeException ex) {
        ApiResponse<Object> response = ApiResponse.error(
                HttpStatus.UNAUTHORIZED.value(),
                "Email hoặc mật khẩu không đúng"
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Object>> handleAppException(AppException ex) {
        ApiResponse<Object> response = new ApiResponse<>(
                ex.getCode(),
                ex.getStatus().value(),  
                ex.getMessage(),
                new Date(),
                null
        );
        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadRequestBody(HttpMessageNotReadableException ex) {
        String detail = ex.getMostSpecificCause() != null
            ? ex.getMostSpecificCause().getMessage()
            : ex.getMessage();

        if (detail != null && detail.length() > 220) {
            detail = detail.substring(0, 220) + "...";
        }

        ApiResponse<Object> response = ApiResponse.error(
                HttpStatus.BAD_REQUEST.value(),
            "Dữ liệu JSON không hợp lệ: " + detail
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleOtherExceptions(Exception ex) {
        ApiResponse<Object> response = new ApiResponse<>(
                "error",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                new Date(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

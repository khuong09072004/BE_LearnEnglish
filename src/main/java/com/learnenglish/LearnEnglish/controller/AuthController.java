package com.learnenglish.LearnEnglish.controller;

import com.learnenglish.LearnEnglish.dto.*;
import com.learnenglish.LearnEnglish.dto.requests.ForgotPasswordRequest;
import com.learnenglish.LearnEnglish.dto.requests.LoginRequest;
import com.learnenglish.LearnEnglish.dto.requests.RegisterRequest;
import com.learnenglish.LearnEnglish.dto.requests.ResendForgotPasswordOtpRequest;
import com.learnenglish.LearnEnglish.dto.requests.ResendOtpRequest;
import com.learnenglish.LearnEnglish.dto.requests.ResetPasswordRequest;
import com.learnenglish.LearnEnglish.dto.requests.VerifyForgotPasswordOtpRequest;
import com.learnenglish.LearnEnglish.dto.requests.VerifyOtpRequest;
import com.learnenglish.LearnEnglish.dto.responses.LoginResponse;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.exception.ValidationException;
import com.learnenglish.LearnEnglish.repository.UserRepository;
import com.learnenglish.LearnEnglish.service.AuthService;

import com.learnenglish.LearnEnglish.util.JwtTokenProvider;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.nio.file.attribute.UserPrincipal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;
    private final AuthService authService;
    // ------------------- LOGIN -------------------
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        // Ném ValidationException, AuthenticationException từ service nếu cần
        User user = authService.Login(request.getEmail(), request.getPassword());

        String token = tokenProvider.generateToken(user);

        return ApiResponse.success("Đăng nhập thành công", new LoginResponse(token));
    }

    // ------------------- REGISTER -------------------
    @PostMapping("/register")
    public ApiResponse<?> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ApiResponse.success("Đăng ký thành công! Vui lòng kiểm tra email để lấy OTP.", null);
    }

    // ------------------- VERIFY OTP -------------------
    @PostMapping("/verify-otp")
    public ApiResponse<String> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        authService.verifyRegistrationOtp(request); // ném ValidationException nếu OTP sai hoặc hết hạn
        return ApiResponse.success("Xác thực OTP thành công! Bạn có thể đăng nhập ngay.", null);
    }

    // ------------------- RESEND OTP -------------------
    @PostMapping("/resend-otp")
    public ApiResponse<String> resendOtp(@Valid @RequestBody ResendOtpRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ValidationException("Không tìm thấy tài khoản với email này"));

        if (user.getStatus() == User.Status.ACTIVE) {
            throw new ValidationException("Tài khoản đã được kích hoạt rồi!");
        }

        authService.resendRegistrationOtp(user); // Gọi service gửi OTP

        return ApiResponse.success("Mã OTP mới đã được gửi đến email của bạn.", null);
    }

    // ------------------- Bước 1: gửi OTP quên mật khẩu -------------------
    @PostMapping("/forgot-password")
    public ApiResponse<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ApiResponse.success("OTP đã được gửi tới email của bạn.", null);
    }

    // ------------------- Bước 2: xác thực OTP -------------------
    @PostMapping("/forgot-password/verify-otp")
    public ApiResponse<String> verifyOtp(@Valid @RequestBody VerifyForgotPasswordOtpRequest request) {
        authService.verifyForgotPasswordOtp(request);
        return ApiResponse.success("OTP hợp lệ. Bạn có thể đặt mật khẩu mới.", null);
    }

    // ------------------- Bước 3: đặt mật khẩu mới -------------------
    @PostMapping("/forgot-password/reset")
    public ApiResponse<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ApiResponse.success("Đặt lại mật khẩu thành công!", null);
    }

    // ------------------- Gửi lại OTP -------------------
    @PostMapping("/forgot-password/resend-otp")
    public ApiResponse<?> resendOtp(@Valid @RequestBody ResendForgotPasswordOtpRequest request) {
        authService.resendForgotPasswordOtp(request.getEmail());
        return ApiResponse.success("Mã OTP mới đã được gửi tới email của bạn.", null);
    }

}


package com.learnenglish.LearnEnglish.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.learnenglish.LearnEnglish.dto.*;
import com.learnenglish.LearnEnglish.dto.requests.ForgotPasswordRequest;
import com.learnenglish.LearnEnglish.dto.requests.GoogleLoginRequest;
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
import com.learnenglish.LearnEnglish.service.GoogleService;
import com.learnenglish.LearnEnglish.util.JwtTokenProvider;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.nio.file.attribute.UserPrincipal;
import java.util.Collections;
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
    @Autowired
    private GoogleService googleService;
   
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = authService.Login(request.getEmail(), request.getPassword());

        String token = tokenProvider.generateToken(user);

        return ApiResponse.success("Đăng nhập thành công", new LoginResponse(token));
    }

    
    @PostMapping("/register")
    public ApiResponse<?> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ApiResponse.success("Đăng ký thành công! Vui lòng kiểm tra email để lấy OTP.", null);
    }

 
    @PostMapping("/verify-otp")
    public ApiResponse<String> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        authService.verifyRegistrationOtp(request); 
        return ApiResponse.success("Xác thực OTP thành công! Bạn có thể đăng nhập ngay.", null);
    }

 
    @PostMapping("/resend-otp")
    public ApiResponse<String> resendOtp(@Valid @RequestBody ResendOtpRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ValidationException("Không tìm thấy tài khoản với email này"));

        if (user.getStatus() == User.Status.ACTIVE) {
            throw new ValidationException("Tài khoản đã được kích hoạt rồi!");
        }

        authService.resendRegistrationOtp(user);

        return ApiResponse.success("Mã OTP mới đã được gửi đến email của bạn.", null);
    }


    @PostMapping("/forgot-password")
    public ApiResponse<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ApiResponse.success("OTP đã được gửi tới email của bạn.", null);
    }

   
    @PostMapping("/forgot-password/verify-otp")
    public ApiResponse<String> verifyOtp(@Valid @RequestBody VerifyForgotPasswordOtpRequest request) {
        authService.verifyForgotPasswordOtp(request);
        return ApiResponse.success("OTP hợp lệ. Bạn có thể đặt mật khẩu mới.", null);
    }


    @PostMapping("/forgot-password/reset")
    public ApiResponse<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ApiResponse.success("Đặt lại mật khẩu thành công!", null);
    }

  
    @PostMapping("/forgot-password/resend-otp")
    public ApiResponse<?> resendOtp(@Valid @RequestBody ResendForgotPasswordOtpRequest request) {
        authService.resendForgotPasswordOtp(request.getEmail());
        return ApiResponse.success("Mã OTP mới đã được gửi tới email của bạn.", null);
    }

    
    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleLoginRequest request) {
        String idToken =request.getIdToken();

        if (idToken == null || idToken.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing id_token"));
        }

        try {
            Map<String, Object> response = googleService.googleLogin(idToken);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

}


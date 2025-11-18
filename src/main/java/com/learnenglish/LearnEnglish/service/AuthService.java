package com.learnenglish.LearnEnglish.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.learnenglish.LearnEnglish.dto.requests.ForgotPasswordRequest;
import com.learnenglish.LearnEnglish.dto.requests.RegisterRequest;
import com.learnenglish.LearnEnglish.dto.requests.ResetPasswordRequest;
import com.learnenglish.LearnEnglish.dto.requests.VerifyForgotPasswordOtpRequest;
import com.learnenglish.LearnEnglish.dto.requests.VerifyOtpRequest;
import com.learnenglish.LearnEnglish.entity.OtpVerification;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.exception.ValidationException;
import com.learnenglish.LearnEnglish.repository.UserRepository;
import jakarta.transaction.Transactional;


@Service
public class AuthService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    OtpService otpService;
    @Autowired
    PasswordEncoder passwordEncoder;

    public User Login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Tài khoản không tồn tại trong hệ thống"));
        if (user.getStatus() == User.Status.PENDING) {
            throw new ValidationException("Tài khoản chưa được kích hoạt. Vui lòng xác thực OTP.");
        } else if (user.getStatus() == User.Status.LOCKED) {
            throw new ValidationException("Tài khoản của bạn đã bị khóa");
        } else if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ValidationException("Mật khẩu không chính xác");
        }
        return user;
    }

    @Transactional
    public void register(RegisterRequest request) {

        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    throw new ValidationException("Email này đã được sử dụng");
                });

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(User.Status.PENDING);
        userRepository.save(user);
        otpService.sendOtpEmail(user, "REGISTER");
    }

    @Transactional
    public void verifyRegistrationOtp(VerifyOtpRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ValidationException("Không tìm thấy tài khoản với email này"));

        otpService.verifyOtp(user.getId(), request.getOtp(), "REGISTER");
        if (user.getStatus() == User.Status.PENDING) {
            user.setStatus(User.Status.ACTIVE);
            userRepository.save(user);
        }
        OtpVerification otp = otpService.getVerifiedOtp(user.getId(), request.getOtp(), "REGISTER").get();
        if (otp.isUsed() == false)
            otp.setUsed(true);
    }

    public void resendRegistrationOtp(User user) {
        if (user.getStatus() == User.Status.ACTIVE) {
            throw new ValidationException("Tài khoản đã được kích hoạt rồi!");
        }
        otpService.sendOtpEmail(user, "REGISTER");
    }

    // Quên mật khẩu: gửi OTP
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ValidationException("Không tìm thấy tài khoản với email này"));
        otpService.sendOtpEmail(user, "FORGOT_PASSWORD");
    }

    // Xác thực OTP
    public void verifyForgotPasswordOtp(VerifyForgotPasswordOtpRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ValidationException("Không tìm thấy tài khoản với email này"));

        otpService.verifyOtp(user.getId(), request.getOtp(), "FORGOT_PASSWORD");
    }

    // Reset mật khẩu
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ValidationException("Không tìm thấy tài khoản với email này"));

        OtpVerification otp = otpService.getVerifiedOtp(user.getId(), request.getOtp(), "FORGOT_PASSWORD")
                .orElseThrow(() -> new ValidationException("Bạn cần xác thực OTP trước khi đặt mật khẩu mới"));
        if (otp.isUsed() == true) {
            throw new ValidationException("OTP này đã được sử dụng");
        } else {
            otp.setUsed(true);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    // Gửi lại OTP 
    public void resendForgotPasswordOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Không tìm thấy tài khoản với email này"));

        otpService.sendOtpEmail(user, "FORGOT_PASSWORD");
    }

}

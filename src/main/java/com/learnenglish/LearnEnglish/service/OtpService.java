package com.learnenglish.LearnEnglish.service;

import com.learnenglish.LearnEnglish.entity.OtpVerification;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.exception.ValidationException;
import com.learnenglish.LearnEnglish.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpService {

    private static final int MAX_OTP_PER_DAY = 5;
    private static final int OTP_COOLDOWN_SECONDS = 60;
    private static final int OTP_EXPIRE_MINUTES = 5;

    @Autowired
    OtpRepository otpRepository;

    @Autowired
    EmailService emailService;

    private String generateOtp() {
        int otp = 100000 + new Random().nextInt(900000);
        return String.format("%06d", otp);
    }

    public void sendOtpEmail(User user, String type) {

        // Kiểm tra số OTP đã gửi hôm nay
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        int sentToday = otpRepository.countOtpSentToday(
                user.getId(), type, startOfDay, endOfDay);

        if (sentToday >= MAX_OTP_PER_DAY) {
            throw new RuntimeException("Bạn vượt quá số lượt OTP  " + type + " hôm nay.");
        }

        // Kiểm tra cooldown
        Optional<OtpVerification> lastOtpOptional = otpRepository
                .findFirstByUserIdAndTypeOrderByCreatedAtDesc(user.getId(), type);

        if (lastOtpOptional.isPresent()) {
            OtpVerification lastOtp = lastOtpOptional.get();
            if (lastOtp.getCreatedAt().isAfter(LocalDateTime.now().minusSeconds(OTP_COOLDOWN_SECONDS))) {
                throw new RuntimeException("Vui lòng đợi" + OTP_COOLDOWN_SECONDS + "giây để tiếp tục gửi mã OTP mới.");
            }
        }

        // Tạo OTP
        String otp = generateOtp();
        OtpVerification token = new OtpVerification();
        token.setUser(user);
        token.setOtpCode(otp);
        token.setExpiresAt(LocalDateTime.now().plusMinutes(OTP_EXPIRE_MINUTES));
        token.setVerified(false);
        token.setType(type);
        token.setCreatedAt(LocalDateTime.now());

        otpRepository.save(token);

        // Gửi OTP qua email
        String subject = type.equals("REGISTER") ? "Xác thực đăng ký" : "Khôi phục mật khẩu";
        String body = "Mã OTP của bạn là: " + otp + "\nOTP có hiệu lực trong " + OTP_EXPIRE_MINUTES + " phút.";
        emailService.send(user.getEmail(), subject, body);
    }

    public void verifyOtp(Long userId, String otpCode, String type) {
        OtpVerification otp = otpRepository.findByUserIdAndTypeAndOtpCodeAndVerifiedFalse(userId, type, otpCode)
                .orElseThrow(() -> new ValidationException("Mã OTP không đúng"));

        if (LocalDateTime.now().isAfter(otp.getExpiresAt())) {
            throw new ValidationException("Mã OTP đã hết hạn");
        }

        otp.setVerified(true);
        otpRepository.save(otp);
    }

    public Optional<OtpVerification> getVerifiedOtp(Long userId, String otpCode, String type) {
        return otpRepository.findByUserIdAndTypeAndOtpCodeAndVerifiedTrue(userId, type, otpCode);
    }

}

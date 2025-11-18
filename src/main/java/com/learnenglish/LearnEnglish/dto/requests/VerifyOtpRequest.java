package com.learnenglish.LearnEnglish.dto.requests;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VerifyOtpRequest {
    private String email;
    private String otp;
}

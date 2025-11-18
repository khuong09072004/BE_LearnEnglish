package com.learnenglish.LearnEnglish.dto.requests;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyForgotPasswordOtpRequest  {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String otp;
}

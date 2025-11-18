package com.learnenglish.LearnEnglish.dto.requests;
import jakarta.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest  {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String otp;

    @NotBlank
    private String newPassword;
}

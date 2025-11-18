package com.learnenglish.LearnEnglish.dto.requests;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResendOtpRequest {
    private String email;
}

package com.learnenglish.LearnEnglish.dto.requests;
import lombok.Data;

@Data
public class UpdatePasswordRequest {
    private String oldPassword;
    private String newPassword;
}

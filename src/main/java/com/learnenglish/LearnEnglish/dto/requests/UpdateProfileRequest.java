package com.learnenglish.LearnEnglish.dto.requests;


import java.time.LocalDate;


import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String fullName;
    private LocalDate dateOfBirth;
}

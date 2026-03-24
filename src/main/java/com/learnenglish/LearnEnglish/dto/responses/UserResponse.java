package com.learnenglish.LearnEnglish.dto.responses;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private String avatar;
    private String role;
    private String status;
    private LocalDate dateOfBirth;
    private String levelCode; 
    private LocalDateTime createdAt;
}
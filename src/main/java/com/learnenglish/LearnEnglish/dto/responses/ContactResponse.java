package com.learnenglish.LearnEnglish.dto.responses;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactResponse {
    private Long id;
    private Long userId;
    private String fullName;
    private String email;
    private String phone;
    private String message;
    private String status;
    private LocalDateTime createdAt;
}

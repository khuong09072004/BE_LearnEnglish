package com.learnenglish.LearnEnglish.dto.requests;

import lombok.Data;

@Data
public class ContactCreateRequest {
    private String fullName;
    private String email;
    private String phone;
    private String message;
}

package com.learnenglish.LearnEnglish.dto.requests;

import lombok.Data;

@Data
public class UserStatusRequest {
    private String status; // Chỉ nhận: ACTIVE, LOCKED
}
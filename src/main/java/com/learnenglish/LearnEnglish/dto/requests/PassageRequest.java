package com.learnenglish.LearnEnglish.dto.requests;

import lombok.Data;

@Data
public class PassageRequest {
    private String title;
    private String content;
    private String category; // READING or LISTENING
}

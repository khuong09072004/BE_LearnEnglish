package com.learnenglish.LearnEnglish.dto.requests;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class PassageRequest {
    private String title;
    @JsonAlias({"passage"})
    private String content;
    private String category; // READING or LISTENING
}

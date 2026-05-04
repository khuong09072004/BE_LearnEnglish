package com.learnenglish.LearnEnglish.dto.requests;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationLessonRequest {
    private String title;
    private String description;
    private Long levelId;
    private String goal;
    private String systemPrompt;
    private LocalDateTime createdAt;
}
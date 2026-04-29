package com.learnenglish.LearnEnglish.dto.responses;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConversationLessonResponse {
    private Long id;
    private String title;
    private String description;
    private Long levelId;
    private String levelName;
    private String skillFocus;
    private String goal;
    private String systemPrompt;
    private LocalDateTime createdAt;
    private List<ConversationStepResponse> steps;
}

package com.learnenglish.LearnEnglish.dto.responses;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConversationStepResponse {
    private Long id;
    private Integer stepOrder;
    private String aiRole;
    private String userTask;
    private String grammarFocus;
    private String sampleAnswer;
    private Integer maxAttempts;
    private LocalDateTime createdAt;
}

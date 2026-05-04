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
public class ConversationStepRequest {
    private Integer stepOrder;
    private String aiRole;
    private String userTask;
    private String grammarFocus;
    private String sampleAnswer;
    private Integer maxAttempts;
    private LocalDateTime createdAt;
}
package com.learnenglish.LearnEnglish.dto.responses;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationSessionResponse {
    private Long id;
    private Long lessonId;
    private String lessonTitle;
    private Long userId;
    private String userName;
    private Integer currentStep;
    private Boolean isCompleted;
    private Boolean isLearn;
    private Integer totalScore;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private List<ConversationTurnResponse> turns;
}
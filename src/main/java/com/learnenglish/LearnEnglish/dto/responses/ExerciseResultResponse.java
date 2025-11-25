package com.learnenglish.LearnEnglish.dto.responses;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;
@Data
public class ExerciseResultResponse {
    private Long resultId;
    private Long exerciseId;
    private String exerciseTitle;
    private int score;
    private int correctCount;
    private JsonNode answers;
    private LocalDateTime completedAt;
}

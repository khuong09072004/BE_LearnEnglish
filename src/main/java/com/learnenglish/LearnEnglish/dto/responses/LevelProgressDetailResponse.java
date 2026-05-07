package com.learnenglish.LearnEnglish.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class LevelProgressDetailResponse {
    private int learnedVocabulary;
    private int totalVocabulary;
    private int completedExercises;
    private int totalExercises;
    private int totalStudyMinutes;
    private double vocabularyPercentage;
    private double exercisePercentage;
    private LocalDateTime lastUpdated;
    private List<TopicProgressDto> topicBreakdown;
    private List<TimeSeriesPointDto> studyHistoryLast7Days;
}

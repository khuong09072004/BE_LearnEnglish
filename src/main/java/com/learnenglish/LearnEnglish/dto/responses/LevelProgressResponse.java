package com.learnenglish.LearnEnglish.dto.responses;

import lombok.*;

@Data
@AllArgsConstructor
public class LevelProgressResponse {
    private int learnedVocabulary;
    private int totalVocabulary;
    private int completedExercises;
    private int totalExercises;
    private int totalStudyMinutes;
}

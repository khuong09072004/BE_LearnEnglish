package com.learnenglish.LearnEnglish.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class StudyHistoryDto {
    private List<TimeSeriesPointDto> dataPoints;
    private String granularity; // "day", "week", "month"
    private int totalMinutes;
    private int totalVocabsLearned;
    private int totalExercisesPassed;
}

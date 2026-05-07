package com.learnenglish.LearnEnglish.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TimeSeriesPointDto {
    private LocalDate date;
    private int minutesSpent;
    private int vocabsLearned;
    private int exercisesPassed;
}

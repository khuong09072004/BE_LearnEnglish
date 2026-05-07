package com.learnenglish.LearnEnglish.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopicProgressDto {
    private Long topicId;
    private String topicName;
    private int learnedVocabulary;
    private int totalVocabulary;
    private double learnedPercentage;
}

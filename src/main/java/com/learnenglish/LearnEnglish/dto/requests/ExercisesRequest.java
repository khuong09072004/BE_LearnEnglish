package com.learnenglish.LearnEnglish.dto.requests;

import lombok.Data;
@Data
public class ExercisesRequest {
    private Long topicId;
    private String title;
    private String type;       
    private int duration;
    private String category;
    private Integer passingId;
}

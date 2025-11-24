package com.learnenglish.LearnEnglish.dto.requests;

import com.fasterxml.jackson.databind.JsonNode;


import lombok.Data;
@Data
public class ExercisesRequest {
    private Long topicId;
    private String title;
    private String type;       
    private JsonNode questions;
    private JsonNode answerKey; 
    private String audioURL;
    private int duration;
    private String category;
}

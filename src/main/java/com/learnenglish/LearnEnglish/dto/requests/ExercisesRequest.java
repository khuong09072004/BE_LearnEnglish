package com.learnenglish.LearnEnglish.dto.requests;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;
@Data
public class ExercisesRequest {
    private Long topicId;
    private String title;
    private String type;       // FILL, MATCH, WRITE...
    private JsonNode questions;
    private String audioURL;
    private int duration;
}

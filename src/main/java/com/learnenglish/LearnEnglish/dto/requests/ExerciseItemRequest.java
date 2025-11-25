package com.learnenglish.LearnEnglish.dto.requests;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class ExerciseItemRequest {
    private Long exerciseId;
    private int position;
    private JsonNode question;
    private JsonNode answer;
}
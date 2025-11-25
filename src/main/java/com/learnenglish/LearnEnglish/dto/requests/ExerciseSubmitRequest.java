package com.learnenglish.LearnEnglish.dto.requests;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;
@Data
public class ExerciseSubmitRequest {
    private Long exerciseId;
    private JsonNode answers;
}

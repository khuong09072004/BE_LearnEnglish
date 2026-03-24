package com.learnenglish.LearnEnglish.dto.responses;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiEvaluation {

    private boolean correct;
    private Double score;

    private JsonNode  analysis;

    private JsonNode  correction;

    private String nextMessage;
}

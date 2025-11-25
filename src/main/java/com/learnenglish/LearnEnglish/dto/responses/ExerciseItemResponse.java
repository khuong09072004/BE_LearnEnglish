package com.learnenglish.LearnEnglish.dto.responses;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseItemResponse {
    private Long id;
    private int position;
    private JsonNode question;
    private JsonNode answer; 
}

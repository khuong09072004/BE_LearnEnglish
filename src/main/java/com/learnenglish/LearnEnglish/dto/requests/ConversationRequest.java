package com.learnenglish.LearnEnglish.dto.requests;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationRequest {
    private Long topicId;
    private String title;
    private String context;
    private JsonNode  roles;  
    private JsonNode  script; 
}

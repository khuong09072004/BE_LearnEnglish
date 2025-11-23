package com.learnenglish.LearnEnglish.dto.responses;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Column;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConverSationRespone {
    private Long id;
    private Long topicId;
    private String title;
    private String context;
    private JsonNode  roles;
    private JsonNode  script;
}

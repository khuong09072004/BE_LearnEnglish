package com.learnenglish.LearnEnglish.dto.responses;
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
    private String roles;
    private String script;
}

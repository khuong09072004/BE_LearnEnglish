package com.learnenglish.LearnEnglish.dto.responses;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConversationLessonListItemResponse {
    private Long id;
    private String title;
    private String description;
    private Long levelId;
    private String levelName;
    private String skillFocus;
    private String goal;
    private LocalDateTime createdAt;

    @JsonProperty("is_learn")
    private Boolean isLearn;
}
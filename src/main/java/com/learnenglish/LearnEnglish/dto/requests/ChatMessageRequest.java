package com.learnenglish.LearnEnglish.dto.requests;

import java.time.LocalDateTime;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageRequest {
    private Long topicId;
    private String content;
}

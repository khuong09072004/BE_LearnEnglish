package com.learnenglish.LearnEnglish.dto.responses;

import java.time.LocalDateTime;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationTurnResponse {

    private Long id;
    private Long sessionId;
    private String role;
    private String content;

    private Object analysis;
    private Object correction;

    private Integer score;
    private LocalDateTime createdAt;
}

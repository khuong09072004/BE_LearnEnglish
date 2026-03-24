package com.learnenglish.LearnEnglish.dto.responses;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiChatResponse {
    private Correction correction;
    private String reply;
}

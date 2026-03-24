package com.learnenglish.LearnEnglish.mapper;

import com.learnenglish.LearnEnglish.dto.responses.ConversationTurnResponse;
import com.learnenglish.LearnEnglish.entity.ConversationTurn;

public class ConversationTurnMapper {
     private ConversationTurnMapper() {}

    public static ConversationTurnResponse toDto(ConversationTurn turn) {
        if (turn == null) return null;

        return ConversationTurnResponse.builder()
            .id(turn.getId())
            .sessionId(turn.getSession() != null ? turn.getSession().getId() : null)
            .role(turn.getRole().name())
            .content(turn.getContent())
            .analysis(turn.getAnalysis())
            .correction(turn.getCorrection())
            .score(turn.getScore())
            .createdAt(turn.getCreatedAt())
            .build();
    }
}

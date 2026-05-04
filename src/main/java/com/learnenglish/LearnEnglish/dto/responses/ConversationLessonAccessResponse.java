package com.learnenglish.LearnEnglish.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationLessonAccessResponse {
    private ConversationLessonResponse lesson;
    private Boolean isLearn;
    private Long learnedSessionId;
    private ConversationSessionResponse history;
}
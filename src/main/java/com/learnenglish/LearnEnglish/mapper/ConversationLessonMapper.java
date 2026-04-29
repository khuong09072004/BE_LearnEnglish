package com.learnenglish.LearnEnglish.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.learnenglish.LearnEnglish.dto.responses.ConversationLessonResponse;
import com.learnenglish.LearnEnglish.dto.responses.ConversationStepResponse;
import com.learnenglish.LearnEnglish.entity.ConversationLesson;
import com.learnenglish.LearnEnglish.entity.ConversationStep;

public class ConversationLessonMapper {

    private ConversationLessonMapper() {}

    public static ConversationLessonResponse toDto(ConversationLesson lesson, List<ConversationStep> steps) {
        List<ConversationStepResponse> stepDtos = steps == null ? List.of() : steps.stream()
            .map(s -> ConversationStepResponse.builder()
                .id(s.getId())
                .stepOrder(s.getStepOrder())
                .aiRole(s.getAiRole())
                .userTask(s.getUserTask())
                .grammarFocus(s.getGrammarFocus())
                .sampleAnswer(s.getSampleAnswer())
                .maxAttempts(s.getMaxAttempts())
                .createdAt(s.getCreatedAt())
                .build())
            .collect(Collectors.toList());

        return ConversationLessonResponse.builder()
            .id(lesson.getId())
            .title(lesson.getTitle())
            .description(lesson.getDescription())
            .levelId(lesson.getLevel() != null ? lesson.getLevel().getId() : null)
            .levelName(lesson.getLevel() != null ? lesson.getLevel().getName() : null)
            .skillFocus(lesson.getSkillFocus() != null ? lesson.getSkillFocus().name() : null)
            .goal(lesson.getGoal())
            .systemPrompt(lesson.getSystemPrompt())
            .createdAt(lesson.getCreatedAt())
            .steps(stepDtos)
            .build();
    }
}

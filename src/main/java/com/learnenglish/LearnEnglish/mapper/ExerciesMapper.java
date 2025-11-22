package com.learnenglish.LearnEnglish.mapper;

import org.springframework.stereotype.Component;

import com.learnenglish.LearnEnglish.dto.responses.ExercisesRespone;
import com.learnenglish.LearnEnglish.entity.Exercises;

@Component
public class ExerciesMapper {
    public ExercisesRespone toDTO(Exercises item)
    {
        return new ExercisesRespone(item.getId(),item.getTopic().getId(),item.getTitle(),item.getType().name(),item.getQuestions(),item.getAudioUrl(),item.getDuration());
    }
}

package com.learnenglish.LearnEnglish.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.learnenglish.LearnEnglish.dto.responses.ExercisesRespone;
import com.learnenglish.LearnEnglish.entity.Exercises;

@Component
public class ExerciesMapper {
     public ExercisesRespone toDTO(Exercises item) {
        return new ExercisesRespone(
            item.getId(),
            item.getTopic().getId(),
            item.getTitle(),
            item.getType().name(),
            item.getQuestions(),
            item.getAudioUrl(),
            item.getDuration(),
            item.getCategory() != null ? item.getCategory().name() : null 
        );
    }


    public List<ExercisesRespone> toListDTO(List<Exercises> lst)
    {
        List<ExercisesRespone> respones=new ArrayList<>();
        for(Exercises item : lst)
        {
            ExercisesRespone dto=toDTO(item);
            respones.add(dto);
        }
        return respones;
    }
}

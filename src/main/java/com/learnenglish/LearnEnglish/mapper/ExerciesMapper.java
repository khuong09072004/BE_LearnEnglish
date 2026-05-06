package com.learnenglish.LearnEnglish.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.learnenglish.LearnEnglish.dto.responses.ExercisesRespone;
import com.learnenglish.LearnEnglish.entity.Exercises;

@Component
public class ExerciesMapper {
     public ExercisesRespone toDTO(Exercises item) {
        ExercisesRespone dto = new ExercisesRespone(
            item.getId(),
            item.getTopic().getId(),
            item.getTitle(),
            item.getType().name(),
            item.getAudioUrl(),
            item.getDuration(),
            item.getCategory() != null ? item.getCategory().name() : null 
        );
        dto.setPassedId(item.getPassingId());
        return dto;
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

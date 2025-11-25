package com.learnenglish.LearnEnglish.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.learnenglish.LearnEnglish.dto.responses.ExerciseItemResponse;
import com.learnenglish.LearnEnglish.dto.responses.ExercisesRespone;
import com.learnenglish.LearnEnglish.entity.ExerciseItems;

@Component
public class ExerciseItemMapper {

    public ExerciseItemResponse toDTO(ExerciseItems item, boolean includeAnswer) {
        return new ExerciseItemResponse(
                item.getId(),
                item.getPosition(),
                item.getQuestionJson(),
                includeAnswer ? item.getAnswerJson() : null);
    }

    public List<ExerciseItemResponse> toListDTO(List<ExerciseItems> lst, boolean includeAnswer) {
        List<ExerciseItemResponse> respones = new ArrayList<>();
        for (ExerciseItems item : lst) {
            ExerciseItemResponse dto = toDTO(item, includeAnswer);
            respones.add(dto);
        }
        return respones;
    }

}

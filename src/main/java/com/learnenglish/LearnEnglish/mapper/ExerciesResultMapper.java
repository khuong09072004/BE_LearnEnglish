package com.learnenglish.LearnEnglish.mapper;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.learnenglish.LearnEnglish.dto.responses.ExerciseResultResponse;
import com.learnenglish.LearnEnglish.entity.ExerciseItems;
import com.learnenglish.LearnEnglish.entity.Exercise_results;
import com.learnenglish.LearnEnglish.repository.ExerciseItemsRepository;

@Component
public class ExerciesResultMapper {
    @Autowired
    ExerciseItemsRepository  exerciseItemsRepository;
    public ExerciseResultResponse toDTO(Exercise_results result,int score) {
        ExerciseResultResponse response = new ExerciseResultResponse();

        response.setResultId(result.getId());
        response.setExerciseId(result.getExercise().getId());
        response.setExerciseTitle(result.getExercise().getTitle());
        response.setScore(score);
        response.setCorrectCount(result.getCorrectCount());
        response.setCompletedAt(result.getCompletedAt());

        ArrayNode answersArray = JsonNodeFactory.instance.arrayNode();

        JsonNode userAnswers = result.getAnswers().get("answers");
        List<ExerciseItems> items =exerciseItemsRepository.findByExercise(result.getExercise()) ;

        for (ExerciseItems item : items) {
            long id = item.getId();
            String correctAns = item.getAnswerJson().get("answer").asText();

            String userAns = "";
            boolean isCorrect = false;

            for (JsonNode userItem : userAnswers) {
                if (userItem.get("id").asLong() == id) {
                    userAns = userItem.get("answer").asText();
                    isCorrect = userAns.trim().equalsIgnoreCase(correctAns.trim());
                }
            }

            ObjectNode ansNode = JsonNodeFactory.instance.objectNode();
            ansNode.put("id", id);
            ansNode.put("answer-you", userAns);
            ansNode.put("answer-correct", correctAns);
            ansNode.put("isCorrect", isCorrect);

            answersArray.add(ansNode);
        }

        response.setAnswers(answersArray);
        return response;
    }
}


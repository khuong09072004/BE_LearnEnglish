package com.learnenglish.LearnEnglish.mapper;

import java.util.ArrayList;
import java.util.List;

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
    private ExerciseItemsRepository exerciseItemsRepository;

    public ExerciseResultResponse toDTO(
            Exercise_results result,
            int score,
            int correctCount
    ) {

        ExerciseResultResponse response = new ExerciseResultResponse();


        response.setResultId(result.getId());
        response.setExerciseId(result.getExercise().getId());
        response.setExerciseTitle(result.getExercise().getTitle());
        response.setScore(score);
        response.setCorrectCount(correctCount);
        response.setCompletedAt(result.getCompletedAt());

        ArrayNode answersArray = JsonNodeFactory.instance.arrayNode();
        JsonNode userAnswersWrapper = result.getAnswers();
        JsonNode userAnswers = userAnswersWrapper.get("answers");

        List<ExerciseItems> items =
                exerciseItemsRepository.findByExercise(result.getExercise());

        for (ExerciseItems item : items) {

            long id = item.getId();
            List<String> correctAnswers = new ArrayList<>();
            JsonNode answerJson = item.getAnswerJson();

            if (answerJson != null) {
                if (answerJson.has("answers") && answerJson.get("answers").isArray()) {
                    for (JsonNode node : answerJson.get("answers")) {
                        correctAnswers.add(node.asText());
                    }
                }

                else if (answerJson.has("answer")) {
                    correctAnswers.add(answerJson.get("answer").asText());
                }
            }

            String userAns = "";
            boolean isCorrect = false;

            if (userAnswers != null && userAnswers.isArray()) {
                for (JsonNode userItem : userAnswers) {
                    if (userItem.get("id").asLong() == id) {
                        userAns = userItem.get("answer").asText().trim();

                       for (String correct : correctAnswers) {
                            if (correct.equalsIgnoreCase(userAns)) {
                                isCorrect = true;
                                break;
                            }
                        }

                        break;
                    }
                }
            }

            ObjectNode ansNode = JsonNodeFactory.instance.objectNode();
            ansNode.put("id", id);
            ansNode.put("answer-you", userAns);

            ArrayNode correctArr = JsonNodeFactory.instance.arrayNode();
            correctAnswers.forEach(correctArr::add);
            ansNode.set("answer-correct", correctArr);

            ansNode.put("isCorrect", isCorrect);

            answersArray.add(ansNode);
        }

        response.setAnswers(answersArray);
        return response;
    }
}

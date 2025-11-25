// package com.learnenglish.LearnEnglish.mapper;

// import java.util.Iterator;

// import org.springframework.stereotype.Component;

// import com.fasterxml.jackson.databind.JsonNode;
// import com.fasterxml.jackson.databind.node.ArrayNode;
// import com.fasterxml.jackson.databind.node.JsonNodeFactory;
// import com.fasterxml.jackson.databind.node.ObjectNode;
// import com.learnenglish.LearnEnglish.dto.responses.ExerciseResultResponse;
// import com.learnenglish.LearnEnglish.entity.Exercise_results;

// @Component
// public class ExerciesResultMapper {

//     public ExerciseResultResponse toDTO(Exercise_results result) {
//         ExerciseResultResponse response = new ExerciseResultResponse();

//         response.setResultId(result.getId());
//         response.setExerciseId(result.getExercise().getId());
//         response.setExerciseTitle(result.getExercise().getTitle());
//         response.setScore(result.getScore());
//         response.setCorrectCount(result.getCorrectCount());
//         response.setCompletedAt(result.getCompletedAt());

//         // Build answers JsonNode
//         ArrayNode answersArray = JsonNodeFactory.instance.arrayNode();
//         JsonNode userAnswers = result.getAnswers().get("answers");
//         JsonNode answerKey = result.getExercise().getAnswerKey().get("answers");

//         for (JsonNode userItem : userAnswers) {
//             long id = userItem.get("id").asLong();
//             String userAns = userItem.get("answer").asText();

//             String correctAns = "";
//             Iterator<JsonNode> it = answerKey.elements();
//             while (it.hasNext()) {
//                 JsonNode keyItem = it.next();
//                 if (keyItem.get("id").asLong() == id) {
//                     correctAns = keyItem.get("answer").asText();
//                     break;
//                 }
//             }

//             ObjectNode ansNode = JsonNodeFactory.instance.objectNode();
//             ansNode.put("id", id);
//             ansNode.put("answer-you", userAns);
//             ansNode.put("answer-correct", correctAns);
//             ansNode.put("isCorrect", userAns.trim().equalsIgnoreCase(correctAns.trim()));

//             answersArray.add(ansNode);
//         }

//         response.setAnswers(answersArray);

//         return response;
//     }
// }

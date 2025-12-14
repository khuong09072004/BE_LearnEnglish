package com.learnenglish.LearnEnglish.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.learnenglish.LearnEnglish.dto.requests.ExerciseSubmitRequest;
import com.learnenglish.LearnEnglish.dto.responses.ExerciseResultResponse;
import com.learnenglish.LearnEnglish.entity.Exercise_results;
import com.learnenglish.LearnEnglish.entity.Exercises;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.mapper.ExerciesResultMapper;
import com.learnenglish.LearnEnglish.repository.ExerciseItemsRepository;
import com.learnenglish.LearnEnglish.repository.ExerciseResultsRepository;
import com.learnenglish.LearnEnglish.repository.ExercisesRepository;
import com.learnenglish.LearnEnglish.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.var;

@Service
public class ExerciseResultService {
    @Autowired
    private ExercisesRepository exercisesRepository;
    @Autowired
    private ExerciseResultsRepository resultsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    ExerciseItemsRepository exerciseItemsRepository;

    @Autowired
    ExerciesResultMapper exerciesResultMapper;

    @Transactional
    public ExerciseResultResponse gradeVocabExercise(
            ExerciseSubmitRequest request, String email) {

        Exercises exercise = getExercise(
                request.getExerciseId(), Exercises.ExerciseCategory.VOCAB);

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        JsonNode userAnswers = getUserAnswers(request);
        var items = exerciseItemsRepository.findByExercise(exercise);

        // Map user answers
        Map<Long, String> answerMap = new HashMap<>();
        for (JsonNode ans : userAnswers) {
            answerMap.put(
                    ans.get("id").asLong(),
                    ans.get("answer").asText().trim().toLowerCase());
        }

        int correctCount = 0;

        for (var item : items) {
            String correct = item.getAnswerJson()
                    .get("answer").asText().trim().toLowerCase();

            String userAns = answerMap.get(item.getId());
            if (userAns != null && userAns.equals(correct)) {
                correctCount++;
            }
        }

        int score = calculateScore(correctCount, items.size());

        Exercise_results result = saveOrUpdateResult(
                exercise, user, request.getAnswers(), correctCount, score);

        return exerciesResultMapper.toDTO(result, score, correctCount);
    }

    @Transactional
    public ExerciseResultResponse gradeGrammarExercise(
            ExerciseSubmitRequest request, String email) {

        Exercises exercise = getExercise(
                request.getExerciseId(), Exercises.ExerciseCategory.GRAMMAR);

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        JsonNode userAnswers = getUserAnswers(request);
        var items = exerciseItemsRepository.findByExercise(exercise);

        Map<Long, String> answerMap = new HashMap<>();
        for (JsonNode ans : userAnswers) {
            answerMap.put(
                    ans.get("id").asLong(),
                    ans.get("answer").asText().trim().toLowerCase());
        }

        int correctCount = 0;

        for (var item : items) {
            String userAns = answerMap.get(item.getId());
            if (userAns == null)
                continue;

            String correct = item.getAnswerJson()
                    .get("answer").asText().trim().toLowerCase();

            if (userAns.equals(correct)) {
                correctCount++;
            }
        }

        int score = calculateScore(correctCount, items.size());

        Exercise_results result = saveOrUpdateResult(
                exercise, user, request.getAnswers(), correctCount, score);

        return exerciesResultMapper.toDTO(result, score, correctCount);
    }

   @Transactional
public ExerciseResultResponse gradeListeningExercise(
        ExerciseSubmitRequest request, String email) {

    Exercises exercise = getExercise(
            request.getExerciseId(),
            Exercises.ExerciseCategory.LISTENING
    );

    var user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User không tồn tại"));

    JsonNode userAnswers = getUserAnswers(request);
    var items = exerciseItemsRepository.findByExercise(exercise);

    // Map: itemId -> userAnswer
    Map<Long, String> answerMap = new HashMap<>();
    for (JsonNode ans : userAnswers) {
        answerMap.put(
                ans.get("id").asLong(),
                ans.get("answer").asText()
        );
    }

    int correctCount = 0;

    for (var item : items) {
        String userAns = answerMap.get(item.getId());
        if (userAns == null) continue;

        boolean isCorrect = checkListeningAnswer(
                item.getAnswerJson(),
                userAns
        );

        if (isCorrect)
            correctCount++;
    }

    int score = calculateScore(correctCount, items.size());

    Exercise_results result = saveOrUpdateResult(
            exercise,
            user,
            request.getAnswers(),
            correctCount,
            score
    );

    try {
        return exerciesResultMapper.toDTO(result, score, correctCount);
    } catch (Exception e) {
        e.printStackTrace();
        throw e;
    }
}


  private boolean checkListeningAnswer(JsonNode answerJson, String userAns) {
    //System.out.println("Correct Answers JSON: " + answerJson);
    if (answerJson == null || !answerJson.has("answers"))
        return false;

    JsonNode answersArray = answerJson.get("answers");
    System.out.println("Answers Array: " + answersArray);
    if (!answersArray.isArray())
        return false;

    userAns = userAns.trim().toLowerCase();

    for (JsonNode node : answersArray) {
        System.out.println("Checking against: " + node.asText());
        String correct = node.asText().trim().toLowerCase();
        if (userAns.equals(correct)) {
            return true;
        }
    }
    return false;
}




    private Exercises getExercise(Long exerciseId, Exercises.ExerciseCategory category) {
        Exercises exercise = exercisesRepository.findById(exerciseId)
                .orElseThrow(() -> new RuntimeException("Exercise không tồn tại"));

        if (exercise.getCategory() != category) {
            throw new RuntimeException("Không đúng dạng bài " + category);
        }
        return exercise;
    }

    private JsonNode getUserAnswers(ExerciseSubmitRequest request) {
        JsonNode wrapper = request.getAnswers();
        if (wrapper == null || wrapper.get("answers") == null) {
            throw new RuntimeException("JSON gửi lên sai cấu trúc");
        }
        return wrapper.get("answers");
    }

    private Exercise_results saveOrUpdateResult(
            Exercises exercise,
            User user,
            JsonNode answers,
            int correctCount,
            int score) {

        var oldOpt = resultsRepository
                .findByExerciseIdAndUserId(exercise.getId(), user.getId());

        Exercise_results result;

        if (oldOpt.isEmpty()) {
            result = new Exercise_results();
            result.setExercise(exercise);
            result.setUser(user);
            result.setAnswers(answers);
            result.setCorrectCount(correctCount);
            result.setScore(score);
            result.setCompletedAt(LocalDateTime.now());
            resultsRepository.save(result);
        } else {
            result = oldOpt.get();
            result.setAnswers(answers);
            result.setCorrectCount(correctCount);

            if (score > result.getScore()) {
                result.setScore(score);
                result.setCompletedAt(LocalDateTime.now());
            }
        }
        return result;
    }

    private int calculateScore(int correct, int total) {
        if (total == 0)
            return 0;
        return (int) Math.round((double) correct / total * 100);
    }

}

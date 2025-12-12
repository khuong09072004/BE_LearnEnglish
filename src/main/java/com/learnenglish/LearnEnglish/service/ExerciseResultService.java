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
import com.learnenglish.LearnEnglish.mapper.ExerciesResultMapper;
import com.learnenglish.LearnEnglish.repository.ExerciseItemsRepository;
import com.learnenglish.LearnEnglish.repository.ExerciseResultsRepository;
import com.learnenglish.LearnEnglish.repository.ExercisesRepository;
import com.learnenglish.LearnEnglish.repository.UserRepository;

import jakarta.transaction.Transactional;

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
    public ExerciseResultResponse gradeVocabExercise(ExerciseSubmitRequest request, String email) {

        Exercises exercise = exercisesRepository.findById(request.getExerciseId())
                .orElseThrow(() -> new RuntimeException("Exercise không tồn tại"));

        if (exercise.getCategory() != Exercises.ExerciseCategory.VOCAB) {
            throw new RuntimeException("Không đúng dạng bài");
        }

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        JsonNode userAnswerWrapper = request.getAnswers();
        if (userAnswerWrapper == null || userAnswerWrapper.get("answers") == null) {
            throw new RuntimeException("kiểu dữ liệu gửi lên không đúng cấu trúc");
        }

        JsonNode userAnswers = userAnswerWrapper.get("answers");
        var items = exerciseItemsRepository.findByExercise(exercise);

        int correctCount = 0;
        int total = items.size();

        // CHẤM BÀI
        for (var item : items) {
            long id = item.getId();
            String correctAns = item.getAnswerJson()
                    .get("answer").asText().trim().toLowerCase();

            for (JsonNode userItem : userAnswers) {
                if (userItem.get("id").asLong() == id) {
                    String userAns = userItem.get("answer").asText().trim().toLowerCase();
                    if (userAns.equals(correctAns))
                        correctCount++;
                }
            }
        }

        int score = (int) Math.round(((double) correctCount / total) * 100);
        var oldResultOpt = resultsRepository.findByExerciseIdAndUserId(
                exercise.getId(), user.getId());

        Exercise_results result;

        if (oldResultOpt.isEmpty()) {

            result = new Exercise_results();
            result.setExercise(exercise);
            result.setUser(user);
            result.setAnswers(userAnswerWrapper);
            result.setCorrectCount(correctCount);
            result.setScore(score);
            result.setCompletedAt(LocalDateTime.now());

            resultsRepository.save(result);

        } else {
            result = oldResultOpt.get();

            result.setAnswers(userAnswerWrapper);
            result.setCorrectCount(correctCount);

            // chỉ update score nếu tốt hơn
            if (score > result.getScore()) {
                result.setScore(score);
                result.setCompletedAt(LocalDateTime.now());
            }

        }

        return exerciesResultMapper.toDTO(result, score, correctCount);
    }

    @Transactional
    public ExerciseResultResponse gradeGrammarExercise(ExerciseSubmitRequest request, String email) {

        Exercises exercise = exercisesRepository.findById(request.getExerciseId())
                .orElseThrow(() -> new RuntimeException("Exercise không tồn tại"));

        if (exercise.getCategory() != Exercises.ExerciseCategory.GRAMMAR) {
            throw new RuntimeException("Không đúng dạng bài Grammar");
        }

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        JsonNode wrapper = request.getAnswers();
        if (wrapper == null || wrapper.get("answers") == null)
            throw new RuntimeException("JSON gửi lên sai format");

        JsonNode userAnswers = wrapper.get("answers");

        var items = exerciseItemsRepository.findByExercise(exercise);

        int correctCount = 0;
        int total = items.size();

        Map<Long, String> answerMap = new HashMap<>();
        for (JsonNode ans : userAnswers) {
            answerMap.put(ans.get("id").asLong(),
                    ans.get("answer").asText().trim().toLowerCase());
        }

        for (var item : items) {

            long id = item.getId();

            if (!answerMap.containsKey(id))
                continue;

            String userAns = answerMap.get(id);
            String correctAns = item.getAnswerJson().get("answer")
                    .asText().trim().toLowerCase();

            JsonNode question = item.getQuestionJson();

            boolean isCorrect = checkGrammarAnswer(question, userAns, correctAns);

            if (isCorrect)
                correctCount++;
        }

        int score = (int) Math.round(((double) correctCount / total) * 100);

        var oldOpt = resultsRepository.findByExerciseIdAndUserId(exercise.getId(), user.getId());
        Exercise_results result;

        if (oldOpt.isEmpty()) {
            result = new Exercise_results();
            result.setExercise(exercise);
            result.setUser(user);
            result.setAnswers(wrapper);
            result.setCorrectCount(correctCount);
            result.setScore(score);
            result.setCompletedAt(LocalDateTime.now());
            resultsRepository.save(result);
        } else {
            result = oldOpt.get();
            result.setAnswers(wrapper);
            result.setCorrectCount(correctCount);

            if (score > result.getScore()) {
                result.setScore(score);
                result.setCompletedAt(LocalDateTime.now());
            }
        }

        return exerciesResultMapper.toDTO(result, score, correctCount);
    }

    private boolean checkGrammarAnswer(JsonNode question, String userAns, String correctAns) {

        userAns = userAns.trim().toLowerCase();
        correctAns = correctAns.trim().toLowerCase();

        if (question.has("options") && question.has("question")) {
            return userAns.equals(correctAns);
        }

        if (question.has("sentences") && question.has("answer_index")) {
            return userAns.equals(correctAns);
        }

        if (question.has("parts")) {
            return userAns.equals(correctAns);
        }

        if (question.has("scrambled")) {
            return userAns.equals(correctAns);
        }

        if (question.has("original") && question.has("instruction")) {
            return userAns.equals(correctAns);
        }

        return userAns.equals(correctAns);
    }

}

package com.learnenglish.LearnEnglish.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.learnenglish.LearnEnglish.dto.enums.ActivityAction;
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
import com.learnenglish.LearnEnglish.util.SaveResultOutcome;

import jakarta.transaction.Transactional;
import lombok.var;

@Service
@Transactional
public class ExerciseResultService {

    @Autowired
    private ExercisesRepository exercisesRepository;
    @Autowired
    private ExerciseResultsRepository resultsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ExerciseItemsRepository exerciseItemsRepository;
    @Autowired
    private ExerciesResultMapper exerciesResultMapper;

    @Autowired
    private UserLevelProgressService userLevelProgressService;

    @Autowired
    private StudyTrackingService studyTrackingService;

    @Autowired
    private ActivityLogService activityLogService;

    public ExerciseResultResponse gradeVocabExercise(
            ExerciseSubmitRequest request, String email) {

        return gradeSingleAnswerExercise(
                request, email, Exercises.ExerciseCategory.VOCAB);
    }

    public ExerciseResultResponse gradeGrammarExercise(
            ExerciseSubmitRequest request, String email) {

        return gradeSingleAnswerExercise(
                request, email, Exercises.ExerciseCategory.GRAMMAR);
    }

    public ExerciseResultResponse gradeListeningExercise(
            ExerciseSubmitRequest request, String email) {

        return gradeMultiAnswerExercise(
                request, email, Exercises.ExerciseCategory.LISTENING);
    }

    public ExerciseResultResponse gradeReadingExercise(
            ExerciseSubmitRequest request, String email) {

        return gradeMultiAnswerExercise(
                request, email, Exercises.ExerciseCategory.READING);
    }

    public ExerciseResultResponse gradeWritingExercise(ExerciseSubmitRequest request, String email) {
        return gradeSingleAnswerExercise(
                request, email, Exercises.ExerciseCategory.WRITING);
    }


    private ExerciseResultResponse gradeSingleAnswerExercise(
            ExerciseSubmitRequest request,
            String email,
            Exercises.ExerciseCategory category) {

        Exercises exercise = getExercise(request.getExerciseId(), category);
        User user = getUser(email);

        Map<Long, String> userAnswerMap = mapUserAnswers(request);
        var items = exerciseItemsRepository.findByExercise(exercise);

        int correctCount = 0;
        for (var item : items) {
            String userAns = userAnswerMap.get(item.getId());
            if (userAns == null)
                continue;

            String correct = item.getAnswerJson()
                    .get("answer").asText().trim().toLowerCase();

            if (userAns.equals(correct)) {
                correctCount++;
            }
        }

        return buildResult(exercise, user, request, correctCount, items.size());
    }

    private ExerciseResultResponse gradeMultiAnswerExercise(
            ExerciseSubmitRequest request,
            String email,
            Exercises.ExerciseCategory category) {

        Exercises exercise = getExercise(request.getExerciseId(), category);
        User user = getUser(email);

        Map<Long, String> userAnswerMap = mapUserAnswers(request);
        var items = exerciseItemsRepository.findByExercise(exercise);

        int correctCount = 0;
        for (var item : items) {
            String userAns = userAnswerMap.get(item.getId());
            if (userAns == null)
                continue;

            if (isCorrectAnswer(
                    item.getAnswerJson(), userAns)) {
                correctCount++;
            }
        }

        return buildResult(exercise, user, request, correctCount, items.size());
    }

   private ExerciseResultResponse buildResult(
        Exercises exercise,
        User user,
        ExerciseSubmitRequest request,
        int correctCount,
        int total) {

    int score = calculateScore(correctCount, total);

    SaveResultOutcome outcome = saveOrUpdateResult(
            exercise,
            user,
            request.getAnswers(),
            correctCount,
            score
    );


    int seconds = request.getTimeSpentInSeconds();
    if (seconds > 0 && seconds <= 3600) {
        int minutes = (int) Math.ceil(seconds / 60.0);
        studyTrackingService.addStudyMinutes(user, minutes);
    }

    if (outcome.firstTime() || outcome.improved()) {
        activityLogService.log(
            user,
            ActivityAction.COMPLETE_EXERCISE,
            """
            {
              "exerciseId": %d,
              "score": %d,
              "correct": %d,
              "type": "%s"
            }
            """.formatted(
                exercise.getId(),
                score,
                correctCount,
                outcome.firstTime() ? "FIRST_TIME" : "IMPROVED"
            )
        );
    }

 
    userLevelProgressService.updateLevelProgress(user, exercise);

    return exerciesResultMapper.toDTO(
        outcome.result(),
        score,
        correctCount
    );
}


    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
    }

    private Exercises getExercise(Long id, Exercises.ExerciseCategory category) {
        Exercises exercise = exercisesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exercise không tồn tại"));

        if (exercise.getCategory() != category) {
            throw new RuntimeException("Không đúng dạng bài " + category);
        }
        return exercise;
    }

    private Map<Long, String> mapUserAnswers(ExerciseSubmitRequest request) {
        JsonNode answers = getUserAnswers(request);

        Map<Long, String> map = new HashMap<>();
        for (JsonNode ans : answers) {
            map.put(
                    ans.get("id").asLong(),
                    ans.get("answer").asText().trim().toLowerCase());
        }
        return map;
    }

    private JsonNode getUserAnswers(ExerciseSubmitRequest request) {
        JsonNode wrapper = request.getAnswers();
        if (wrapper == null || !wrapper.has("answers")) {
            throw new RuntimeException("JSON gửi lên sai cấu trúc");
        }
        return wrapper.get("answers");
    }

    private boolean isCorrectAnswer(JsonNode answerJson, String userAns) {

        if (answerJson == null || userAns == null)
            return false;

        userAns = userAns.trim().toLowerCase();

        if (answerJson.has("answer")) {
            String correct = answerJson.get("answer")
                    .asText()
                    .trim()
                    .toLowerCase();

            return userAns.equals(correct);
        }

        if (answerJson.has("answers")) {
            JsonNode answersArray = answerJson.get("answers");
            if (!answersArray.isArray())
                return false;

            for (JsonNode node : answersArray) {
                String correct = node.asText().trim().toLowerCase();
                if (userAns.equals(correct)) {
                    return true;
                }
            }
        }

        return false;
    }

        private SaveResultOutcome saveOrUpdateResult(
            Exercises exercise,
            User user,
            JsonNode answers,
            int correctCount,
            int score) {

        var oldOpt = resultsRepository
                .findByExerciseIdAndUserId(exercise.getId(), user.getId());

        Exercise_results result;
        boolean firstTime = false;
        boolean improved = false;

        if (oldOpt.isEmpty()) {
            result = new Exercise_results();
            result.setExercise(exercise);
            result.setUser(user);
            firstTime = true;
        } else {
            result = oldOpt.get();
            if (score > result.getScore()) {
                improved = true;
            }
        }

        result.setAnswers(answers);
        result.setCorrectCount(correctCount);

        // chỉ update score + completedAt khi có ý nghĩa
        if (firstTime || improved) {
            result.setScore(score);
            result.setCompletedAt(LocalDateTime.now());
        }

        resultsRepository.save(result);

        return new SaveResultOutcome(result, firstTime, improved);
}


    private int calculateScore(int correct, int total) {
        if (total == 0)
            return 0;
        return (int) Math.round((double) correct / total * 100);
    }

    
}

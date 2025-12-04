package com.learnenglish.LearnEnglish.service;

import java.time.LocalDateTime;

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
                if (userAns.equals(correctAns)) correctCount++;
            }
        }
    }

    int score = (int) Math.round(((double) correctCount / total) * 100);
    var oldResultOpt = resultsRepository.findByExerciseIdAndUserId(
            exercise.getId(), user.getId()
    );

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

        if (score > result.getScore()) {
            result.setAnswers(userAnswerWrapper);
            result.setCorrectCount(correctCount);
            result.setScore(score);
            result.setCompletedAt(LocalDateTime.now());

            resultsRepository.save(result);
        }
      
    }

    return exerciesResultMapper.toDTO(result,score,correctCount);
}


}

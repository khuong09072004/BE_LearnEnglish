// package com.learnenglish.LearnEnglish.service;

// import java.time.LocalDateTime;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.fasterxml.jackson.databind.JsonNode;
// import com.learnenglish.LearnEnglish.dto.requests.ExerciseSubmitRequest;
// import com.learnenglish.LearnEnglish.dto.responses.ExerciseResultResponse;
// import com.learnenglish.LearnEnglish.entity.Exercise_results;
// import com.learnenglish.LearnEnglish.entity.Exercises;
// import com.learnenglish.LearnEnglish.mapper.ExerciesResultMapper;
// import com.learnenglish.LearnEnglish.repository.ExerciseResultsRepository;
// import com.learnenglish.LearnEnglish.repository.ExercisesRepository;
// import com.learnenglish.LearnEnglish.repository.UserRepository;

// import jakarta.transaction.Transactional;

// @Service
// public class ExerciseResultService {
//     @Autowired
//     private  ExercisesRepository exercisesRepository;
//     @Autowired
//     private  ExerciseResultsRepository resultsRepository;
//     @Autowired
//     private  UserRepository userRepository;

//     @Autowired
//     ExerciesResultMapper exerciesResultMapper;
// @Transactional
//      public ExerciseResultResponse gradeVocabExercise(ExerciseSubmitRequest request, String email) {
//         Exercises exercise = exercisesRepository.findById(request.getExerciseId())
//                 .orElseThrow(() -> new RuntimeException("Exercise not found"));

//         if (exercise.getCategory() != Exercises.ExerciseCategory.VOCAB) {
//             throw new RuntimeException("Exercise is not VOCAB category");
//         }

//         JsonNode userAnswers = request.getAnswers();
//         JsonNode answerKey = exercise.getAnswerKey().get("answers");

//         int correctCount = 0;
//         int total = answerKey.size();

//         // Loop chấm bài theo type
//         switch (exercise.getType()) {
//             case VOCAB_MATCH:
//             case VOCAB_MEANING_CHOICE:
//             case VOCAB_PHONETIC:
//             case VOCAB_PATTERN_FILL:
//                 correctCount = gradeSimpleVocab(userAnswers, answerKey);
//                 break;
//             default:
//                 throw new RuntimeException("Unsupported VOCAB type");
//         }

//         int score = (int) Math.round(((double) correctCount / total) * 100);

//         // Lưu kết quả
//         Exercise_results result = new Exercise_results();
//         result.setExercise(exercise);
//         result.setUser(userRepository.findByEmail(email).orElse(null));
//         result.setAnswers(userAnswers);
//         result.setCorrectCount(correctCount);
//         result.setScore(score);
//         result.setCompletedAt(LocalDateTime.now());
//         resultsRepository.save(result);
//         return exerciesResultMapper.toDTO(result);
//     }

//     private int gradeSimpleVocab(JsonNode userAnswers, JsonNode answerKey) {
//     int correct = 0;

//     for (JsonNode correctItem : answerKey) {
//         long id = correctItem.get("id").asLong();
//         String correctAns = correctItem.get("answer").asText().trim().toLowerCase();

//         for (JsonNode userItem : userAnswers.get("answers")) {
//             if (userItem.get("id").asLong() == id) {
//                 String userAns = userItem.get("answer").asText().trim().toLowerCase();

//                 if (userAns.equals(correctAns)) {
//                     correct++;
//                 }
//             }
//         }
//     }
//     return correct;
// }

// }

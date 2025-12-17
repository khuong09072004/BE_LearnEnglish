package com.learnenglish.LearnEnglish.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnenglish.LearnEnglish.dto.responses.LevelProgressResponse;
import com.learnenglish.LearnEnglish.dto.responses.TopicsRespone;
import com.learnenglish.LearnEnglish.entity.Levels;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.entity.User_level_progress;
import com.learnenglish.LearnEnglish.exception.ValidationException;
import com.learnenglish.LearnEnglish.repository.ExerciseResultsRepository;
import com.learnenglish.LearnEnglish.repository.ExercisesRepository;
import com.learnenglish.LearnEnglish.repository.LevelsRepository;
import com.learnenglish.LearnEnglish.repository.StudyTrackingRepository;
import com.learnenglish.LearnEnglish.repository.UserLevelProgressRepository;
import com.learnenglish.LearnEnglish.repository.UserRepository;
import com.learnenglish.LearnEnglish.repository.UserVocabProgressRepository;
import com.learnenglish.LearnEnglish.repository.VocabulariesRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LevelsRepository levelsRepository;

    @Autowired
    private UserLevelProgressRepository userLevelProgressRepository;
    @Autowired
    private VocabulariesRepository vocabulariesRepository;
    @Autowired
    private UserVocabProgressRepository userVocabProgressRepository;
    @Autowired
    private  ExercisesRepository exercisesRepository;
    @Autowired
    private  ExerciseResultsRepository exerciseResultsRepository;
    @Autowired
    private  StudyTrackingRepository studyTrackingRepository;


    @Transactional
    public void selectLevel(String levelCode, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ValidationException("Không tìm thấy tài khoản"));

        if (Boolean.TRUE.equals(user.getHasSelectedLevel())) {
            throw new ValidationException("User đã chọn level rồi");
        }

        Levels selectedLevel = levelsRepository.findByCode(levelCode)
                .orElseThrow(() ->
                        new ValidationException("Level không hợp lệ"));

        // set level hiện tại
        user.setLevel(selectedLevel);
        user.setCurrentLevel(selectedLevel);
        user.setHasSelectedLevel(true);
        userRepository.save(user);

        //  các level nhỏ hơn → completed
        List<Levels> lowerLevels =
                levelsRepository.findByLevelOrderLessThan(
                        selectedLevel.getLevelOrder());

        for (Levels l : lowerLevels) {
            userLevelProgressRepository.save(
                new User_level_progress(
                    null,
                    user,
                    l,
                    0,
                    true,
                    LocalDateTime.now()
                )
            );
        }

        //  level hiện tại → chưa completed
        userLevelProgressRepository.save(
            new User_level_progress(
                null,
                user,
                selectedLevel,
                0,
                false,
                null
            )
        );
    }

    // get tracking in Level 
     public LevelProgressResponse getLevelProgress( String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        Long levelId = user.getCurrentLevel().getId();
        int totalVocabulary =
                vocabulariesRepository.countByLevel(levelId);

        int learnedVocabulary =
                userVocabProgressRepository
                        .countLearnedVocabularyByLevel(user, levelId);
        int totalExercises =
                exercisesRepository.countByLevel(levelId);

        int passedExercises =
                exerciseResultsRepository
                        .countPassedExercisesByLevel(user, levelId);

        int totalStudyMinutes =
                studyTrackingRepository.sumStudyMinutes(user);

        return new LevelProgressResponse(
                learnedVocabulary,
                totalVocabulary,
                passedExercises,
                totalExercises,
                totalStudyMinutes
        );
    }
}


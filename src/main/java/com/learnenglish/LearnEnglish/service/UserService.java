package com.learnenglish.LearnEnglish.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.learnenglish.LearnEnglish.dto.requests.UpdatePasswordRequest;
import com.learnenglish.LearnEnglish.dto.requests.UpdateProfileRequest;
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

import jakarta.mail.Multipart;
import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;

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
    @Autowired
    private CloudinaryService cloudinaryService;

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

     public Object getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("User không tồn tại"));
        Map<String, Object> userProfile = new HashMap<>();
        userProfile.put("name", user.getFullName());
        userProfile.put("email", user.getEmail());
        userProfile.put("avatar", user.getAvatar());
        userProfile.put("dob", user.getDateOfBirth());
        return userProfile;
     }

public String updateAvatar(String email, MultipartFile avatar) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("User không tồn tại"));

        String avatarUrl = cloudinaryService.uploadImage(avatar);
        if (avatarUrl == null) {
                throw new ValidationException("Cập nhật avatar thất bại");
        }

        user.setAvatar(avatarUrl);
        userRepository.save(user);

        return avatarUrl;
        }

public Object updateProfile(String name, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(name)
                .orElseThrow(() -> new ValidationException("User không tồn tại"));
        if (request.getFullName() != null) {
                user.setFullName(request.getFullName());
        }

        if (request.getDateOfBirth() != null) {
                user.setDateOfBirth(request.getDateOfBirth());
        }

        
        userRepository.save(user);
        Map<String, Object> userProfile = new HashMap<>();
        userProfile.put("name", user.getFullName());
        userProfile.put("email", user.getEmail());
        userProfile.put("avatar", user.getAvatar());
        userProfile.put("dob", user.getDateOfBirth());
        return userProfile;
}

public void changePassword(String email, UpdatePasswordRequest request) {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ValidationException("User không tồn tại"));

    if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
        throw new ValidationException("Mật khẩu cũ không đúng");
    }

    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);
}
}



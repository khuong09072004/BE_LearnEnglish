package com.learnenglish.LearnEnglish.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.learnenglish.LearnEnglish.dto.requests.UpdatePasswordRequest;
import com.learnenglish.LearnEnglish.dto.requests.UpdateProfileRequest;
import com.learnenglish.LearnEnglish.dto.responses.LevelProgressDetailResponse;
import com.learnenglish.LearnEnglish.dto.responses.LevelProgressResponse;
import com.learnenglish.LearnEnglish.dto.responses.StudyHistoryDto;
import com.learnenglish.LearnEnglish.dto.responses.TimeSeriesPointDto;
import com.learnenglish.LearnEnglish.dto.responses.TopicProgressDto;
import com.learnenglish.LearnEnglish.dto.responses.TopicsRespone;
import com.learnenglish.LearnEnglish.entity.Exercise_results;
import com.learnenglish.LearnEnglish.entity.Levels;
import com.learnenglish.LearnEnglish.entity.Study_tracking;
import com.learnenglish.LearnEnglish.entity.Topics;
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
        private ExercisesRepository exercisesRepository;
        @Autowired
        private ExerciseResultsRepository exerciseResultsRepository;
        @Autowired
        private StudyTrackingRepository studyTrackingRepository;
        @Autowired
        private CloudinaryService cloudinaryService;

        @Transactional
        public void selectLevel(String levelCode, String email) {

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new ValidationException("Không tìm thấy tài khoản"));

                if (Boolean.TRUE.equals(user.getHasSelectedLevel())) {
                        throw new ValidationException("User đã chọn level rồi");
                }

                Levels selectedLevel = levelsRepository.findByCode(levelCode)
                                .orElseThrow(() -> new ValidationException("Level không hợp lệ"));

                // set level hiện tại
                user.setLevel(selectedLevel);
                user.setCurrentLevel(selectedLevel);
                user.setHasSelectedLevel(true);
                userRepository.save(user);

                // các level nhỏ hơn → completed
                List<Levels> lowerLevels = levelsRepository.findByLevelOrderLessThan(
                                selectedLevel.getLevelOrder());

                for (Levels l : lowerLevels) {
                        userLevelProgressRepository.save(
                                        new User_level_progress(
                                                        null,
                                                        user,
                                                        l,
                                                        0,
                                                        true,
                                                        LocalDateTime.now()));
                }

                // level hiện tại → chưa completed
                userLevelProgressRepository.save(
                                new User_level_progress(
                                                null,
                                                user,
                                                selectedLevel,
                                                0,
                                                false,
                                                null));
        }

        // get tracking in Level
        public LevelProgressResponse getLevelProgress(String email) {

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
                Long levelId = user.getCurrentLevel().getId();
                int totalVocabulary = vocabulariesRepository.countByLevel(levelId);

                int learnedVocabulary = userVocabProgressRepository
                                .countLearnedVocabularyByLevel(user, levelId);
                int totalExercises = exercisesRepository.countByLevel(levelId);

                int passedExercises = exerciseResultsRepository
                                .countPassedExercisesByLevel(user, levelId);

                int totalStudyMinutes = studyTrackingRepository.sumStudyMinutes(user);

                return new LevelProgressResponse(
                                learnedVocabulary,
                                totalVocabulary,
                                passedExercises,
                                totalExercises,
                                totalStudyMinutes);
        }

        public Object getProfile(String email) {
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new ValidationException("User không tồn tại"));
                Map<String, Object> userProfile = new HashMap<>();
                userProfile.put("name", user.getFullName());
                userProfile.put("email", user.getEmail());
                userProfile.put("avatar", user.getAvatar());
                userProfile.put("dob", user.getDateOfBirth());
                userProfile.put("TypeAccount", user.getGoogleId() == null ? "Normal" : "Google");
                userProfile.put("Level", user.getLevel() != null ? user.getLevel().getCode() : "Chưa chọn cấp độ");
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

        // Get detailed level progress with breakdown by topic and 7-day history
        public LevelProgressDetailResponse getLevelProgressDetail(String email) {
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
                Long levelId = user.getCurrentLevel().getId();

                int totalVocabulary = vocabulariesRepository.countByLevel(levelId);
                int learnedVocabulary = userVocabProgressRepository.countLearnedVocabularyByLevel(user, levelId);
                int totalExercises = exercisesRepository.countByLevel(levelId);
                int passedExercises = exerciseResultsRepository.countPassedExercisesByLevel(user, levelId);
                int totalStudyMinutes = studyTrackingRepository.sumStudyMinutes(user);

                double vocabPercentage = totalVocabulary > 0 ? (learnedVocabulary * 100.0 / totalVocabulary) : 0;
                double exercisePercentage = totalExercises > 0 ? (passedExercises * 100.0 / totalExercises) : 0;

                // Breakdown per topic
                List<Topics> allTopics = vocabulariesRepository.findAllTopicsByLevel(levelId);
                List<TopicProgressDto> topicBreakdown = allTopics.stream().map(topic -> {
                        int topicVocabTotal = vocabulariesRepository.countByTopicId(topic.getId());
                        int topicVocabLearned = userVocabProgressRepository.countLearnedByUserAndTopic(user.getId(), topic.getId());
                        double topicPercent = topicVocabTotal > 0 ? (topicVocabLearned * 100.0 / topicVocabTotal) : 0;
                        return new TopicProgressDto(topic.getId(), topic.getName(), topicVocabLearned, topicVocabTotal, topicPercent);
                }).collect(Collectors.toList());

                // Study history for last 7 days
                LocalDate endDate = LocalDate.now();
                LocalDate startDate = endDate.minusDays(6);
                List<Study_tracking> historyData = studyTrackingRepository.findByUserAndDateRange(user, startDate, endDate);
                List<Exercise_results> exerciseHistory = exerciseResultsRepository.findByUserAndDateRange(user, startDate, endDate);
                
                Map<LocalDate, Integer> dateVsMinutes = new HashMap<>();
                Map<LocalDate, Integer> dateVsExercisesPassed = new HashMap<>();
                for (LocalDate d = startDate; d.isBefore(endDate.plusDays(1)); d = d.plusDays(1)) {
                        dateVsMinutes.put(d, 0);
                        dateVsExercisesPassed.put(d, 0);
                }
                historyData.forEach(st -> dateVsMinutes.put(st.getStudyDate(), st.getMinutesSpent()));
                exerciseHistory.stream()
                        .filter(result -> result.getScore() >= 80 && result.getCompletedAt() != null)
                        .forEach(result -> dateVsExercisesPassed.merge(
                                result.getCompletedAt().toLocalDate(),
                                1,
                                Integer::sum));

                List<TimeSeriesPointDto> studyHistory = dateVsMinutes.entrySet().stream()
                        .map(entry -> new TimeSeriesPointDto(
                                entry.getKey(),
                                entry.getValue(),
                                0,
                                dateVsExercisesPassed.getOrDefault(entry.getKey(), 0)))
                        .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                        .collect(Collectors.toList());

                return new LevelProgressDetailResponse(
                        learnedVocabulary,
                        totalVocabulary,
                        passedExercises,
                        totalExercises,
                        totalStudyMinutes,
                        vocabPercentage,
                        exercisePercentage,
                        LocalDateTime.now(),
                        topicBreakdown,
                        studyHistory
                );
        }

        // Get study history with custom date range and granularity
        public StudyHistoryDto getLevelProgressHistory(String email, LocalDate startDate, LocalDate endDate, String granularity) {
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

                List<Study_tracking> rawData = studyTrackingRepository.findByUserAndDateRange(user, startDate, endDate);
                List<Exercise_results> exerciseHistory = exerciseResultsRepository.findByUserAndDateRange(user, startDate, endDate);
                Map<LocalDate, Integer> dateMap = new HashMap<>();
                Map<LocalDate, Integer> exercisePassMap = new HashMap<>();
                for (LocalDate d = startDate; d.isBefore(endDate.plusDays(1)); d = d.plusDays(1)) {
                        dateMap.put(d, 0);
                        exercisePassMap.put(d, 0);
                }
                rawData.forEach(st -> dateMap.put(st.getStudyDate(), st.getMinutesSpent()));
                exerciseHistory.stream()
                        .filter(result -> result.getScore() >= 80 && result.getCompletedAt() != null)
                        .forEach(result -> exercisePassMap.merge(
                                result.getCompletedAt().toLocalDate(),
                                1,
                                Integer::sum));

                List<TimeSeriesPointDto> dataPoints = dateMap.entrySet().stream()
                        .map(entry -> new TimeSeriesPointDto(
                                entry.getKey(),
                                entry.getValue(),
                                0,
                                exercisePassMap.getOrDefault(entry.getKey(), 0)))
                        .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                        .collect(Collectors.toList());

                int totalMinutes = rawData.stream().mapToInt(Study_tracking::getMinutesSpent).sum();
                int totalExercisesPassed = exerciseHistory.stream()
                        .filter(result -> result.getScore() >= 80 && result.getCompletedAt() != null)
                        .mapToInt(result -> 1)
                        .sum();

                return new StudyHistoryDto(
                        dataPoints,
                        granularity != null ? granularity : "day",
                        totalMinutes,
                        0,
                        totalExercisesPassed
                );
        }

        // Get topic progress details
        public TopicProgressDto getTopicProgress(String email, Long topicId) {
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new ValidationException("User không tồn tại"));
                
                Topics topic = vocabulariesRepository.findByTopicId(topicId).stream()
                        .map(v -> v.getTopic())
                        .findFirst()
                        .orElseThrow(() -> new ValidationException("Topic không tồn tại"));

                int totalVocab = vocabulariesRepository.countByTopicId(topicId);
                int learnedVocab = userVocabProgressRepository.countLearnedByUserAndTopic(user.getId(), topicId);
                double percentage = totalVocab > 0 ? (learnedVocab * 100.0 / totalVocab) : 0;

                return new TopicProgressDto(topicId, topic.getName(), learnedVocab, totalVocab, percentage);
        }
}

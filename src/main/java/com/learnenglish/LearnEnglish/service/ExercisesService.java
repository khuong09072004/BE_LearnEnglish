package com.learnenglish.LearnEnglish.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.learnenglish.LearnEnglish.dto.enums.ExerciseType;
import com.learnenglish.LearnEnglish.dto.requests.ExercisesRequest;
import com.learnenglish.LearnEnglish.dto.responses.ExercisesRespone;
import com.learnenglish.LearnEnglish.entity.Exercises;
import com.learnenglish.LearnEnglish.entity.Levels;
import com.learnenglish.LearnEnglish.entity.Topics;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.exception.ValidationException;
import com.learnenglish.LearnEnglish.mapper.ExerciesMapper;
import com.learnenglish.LearnEnglish.repository.ExerciseResultsRepository;
import com.learnenglish.LearnEnglish.repository.ExercisesRepository;
import com.learnenglish.LearnEnglish.repository.PassagesRepository;
import com.learnenglish.LearnEnglish.repository.TopicsRepository;
import com.learnenglish.LearnEnglish.repository.UserRepository;
import com.learnenglish.LearnEnglish.util.UserLevelHelper;

import jakarta.transaction.Transactional;

@Service
public class ExercisesService {

    @Autowired
    private ExercisesRepository exercisesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicsRepository topicsRepository;

    @Autowired
    private ExerciesMapper exerciesMapper;

    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private UserLevelHelper userLevelHelper;

    @Autowired
    private PassagesRepository passagesRepository;
   
    public List<ExercisesRespone> getExercies(String email, Long topicId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Không tìm thấy tài khoản với email này"));

        Topics topic = topicsRepository.findById(topicId)
                .orElseThrow(() -> new ValidationException("Không tìm thấy Topics"));
        Levels studyingLevel = userLevelHelper.getStudyingLevel(user);
        if (!studyingLevel.getCode().equals(topic.getLevel().getCode())) {
            throw new ValidationException("Topic không phù hợp với trình độ của người học");
        }

        List<Object[]> rows = exercisesRepository.findWithResultRaw(topicId, user.getId());
        return rows.stream().map(r -> {
        ExercisesRespone dto = new ExercisesRespone(
            (Long) r[0],               
            (Long) r[1],                 
            (String) r[2],              
            r[3].toString(),              
            (String) r[4],                  
            (Integer) r[5],                 
            r[6] != null ? r[6].toString() : null 
        );

        dto.setScore(r[7] != null ? (Integer) r[7] : 0);
        dto.setIsDone(r[8] != null ? (Boolean) r[8] : false);
        dto.setPassedId(r[9] == null ? null : ((Number) r[9]).intValue());
        return dto;
    }).toList();
    }

   
    public ExercisesRespone getExerciesById(Long id) {
        Exercises exercise = exercisesRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Không tìm thấy Exercises"));
        return exerciesMapper.toDTO(exercise);
    }

   
    @Transactional
    public ExercisesRespone createExercise(ExercisesRequest req, MultipartFile audioFile) {
        ExerciseType typeEnum = parseType(req.getType());

        Exercises exercise = buildExercise(req, typeEnum);
        applyPassageRule(exercise, typeEnum, req.getPassingId());
        handleAudioUpload(exercise, typeEnum, audioFile);

        exercisesRepository.save(exercise);
        return exerciesMapper.toDTO(exercise);
    }

   
    @Transactional
    public ExercisesRespone updateExercise(Long id, ExercisesRequest req, MultipartFile audioFile) {
        Exercises exercise = exercisesRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Exercise không tồn tại"));

        ExerciseType typeEnum = parseType(req.getType());
        Topics topic = topicsRepository.findById(req.getTopicId())
                .orElseThrow(() -> new ValidationException("Topic không tồn tại"));

        exercise.setTopic(topic);
        exercise.setTitle(req.getTitle());
        exercise.setType(typeEnum);
        exercise.setDuration(req.getDuration());

        if (req.getCategory() != null) {
            exercise.setCategory(Exercises.ExerciseCategory.valueOf(req.getCategory().toUpperCase()));
        }

        applyPassageRule(exercise, typeEnum, req.getPassingId());

        handleAudioUpload(exercise, typeEnum, audioFile);

        exercisesRepository.save(exercise);
        return exerciesMapper.toDTO(exercise);
    }

  
    @Transactional
    public ExercisesRespone deleteExercise(Long id) {
        Exercises exercise = exercisesRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Exercise không tồn tại"));

        exercisesRepository.delete(exercise);
        return exerciesMapper.toDTO(exercise);
    }

  
    private ExerciseType parseType(String type) {
        if (type == null || type.isBlank()) {
            throw new ValidationException("Type không được để trống");
        }

        try {
            return ExerciseType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Type không hợp lệ: " + type);
        }
    }

    private Exercises buildExercise(ExercisesRequest req, ExerciseType typeEnum) {
        Topics topic = topicsRepository.findById(req.getTopicId())
                .orElseThrow(() -> new ValidationException("Topic không tồn tại"));

        Exercises exercise = new Exercises();

        exercise.setTopic(topic);
        exercise.setTitle(req.getTitle());
        exercise.setType(typeEnum);
        exercise.setDuration(req.getDuration());

        if (req.getCategory() != null) {
            exercise.setCategory(Exercises.ExerciseCategory.valueOf(req.getCategory().toUpperCase()));
        }

        return exercise;
    }

    private void handleAudioUpload(Exercises exercise, ExerciseType typeEnum, MultipartFile audioFile) {
        if (exercise.getCategory() == Exercises.ExerciseCategory.LISTENING) {
            if (audioFile == null || audioFile.isEmpty()) {
                throw new ValidationException("Bài nghe yêu cầu upload audio");
            }

            String audioUrl = cloudinaryService.uploadAudio(audioFile);
            exercise.setAudioUrl(audioUrl);
            return;
        }

        if (isAudioExercise(typeEnum)) {
            if (audioFile != null && !audioFile.isEmpty()) {
                String audioUrl = cloudinaryService.uploadAudio(audioFile);
                exercise.setAudioUrl(audioUrl);
            }
        } else {
            exercise.setAudioUrl(null);
        }
    }

    private boolean isAudioExercise(ExerciseType type) {
        switch (type) {
            case LISTEN_MCQ:
            case LISTEN_FILL:
            case LISTEN_QA:
            case LISTEN_WRITE:
                return true;
            default:
                return false;
        }
    }

    private boolean isReadingExercise(ExerciseType type) {
        switch (type) {
            case READ_MCQ:
            case READ_FILL:
            case READ_QA:
                return true;
            default:
                return false;
        }
    }

    private void applyPassageRule(Exercises exercise, ExerciseType type, Integer passingId) {
        boolean isReading = isReadingExercise(type) || exercise.getCategory() == Exercises.ExerciseCategory.READING;

        if (!isReading) {
            // Non-reading exercises do not require passage link.
            exercise.setPassingId(null);
            return;
        }

        if (passingId == null) {
            throw new ValidationException("Bài Reading bắt buộc phải chọn passageId");
        }

        passagesRepository.findById(Long.valueOf(passingId))
                .orElseThrow(() -> new ValidationException("Passage không tồn tại: " + passingId));
        exercise.setPassingId(passingId);
    }

  
    public List<ExercisesRespone> getExercisesByTopicAndCategory(String email, Long topicId, String categoryStr) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Không tìm thấy tài khoản với email này"));

        Topics topic = topicsRepository.findById(topicId)
                .orElseThrow(() -> new ValidationException("Không tìm thấy Topics"));
        Levels studyingLevel = userLevelHelper.getStudyingLevel(user);
        if (!studyingLevel.getCode().equals(topic.getLevel().getCode())) {
            throw new ValidationException("Topic không phù hợp với trình độ của người học");
        }

        Exercises.ExerciseCategory category;
        try {
            category = Exercises.ExerciseCategory.valueOf(categoryStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Category không hợp lệ: " + categoryStr);
        }

        List<Object[]> rows = exercisesRepository.findByTopicCategoryRaw(topicId, category, user.getId());

    return rows.stream().map(r -> {
        ExercisesRespone dto = new ExercisesRespone(
                (Long) r[0],
                (Long) r[1],
                (String) r[2],
                r[3].toString(),
                (String) r[4],
                (Integer) r[5],
                r[6] != null ? r[6].toString() : null
        );

        dto.setScore(r[7] != null ? (Integer) r[7] : 0);
        dto.setIsDone(r[8] != null ? (Boolean) r[8] : false);
        dto.setPassedId(r[9] != null ? (Integer) r[9] : null);
        return dto;
    }).toList();
    }
}

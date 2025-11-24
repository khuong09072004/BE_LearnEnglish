package com.learnenglish.LearnEnglish.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.learnenglish.LearnEnglish.dto.requests.ExercisesRequest;
import com.learnenglish.LearnEnglish.dto.responses.ExercisesRespone;
import com.learnenglish.LearnEnglish.entity.Exercises;
import com.learnenglish.LearnEnglish.entity.Topics;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.exception.ValidationException;
import com.learnenglish.LearnEnglish.mapper.ExerciesMapper;
import com.learnenglish.LearnEnglish.repository.ExercisesRepository;
import com.learnenglish.LearnEnglish.repository.TopicsRepository;
import com.learnenglish.LearnEnglish.repository.UserRepository;

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

   
    public List<ExercisesRespone> getExercies(String email, Long topicId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Không tìm thấy tài khoản với email này"));

        Topics topic = topicsRepository.findById(topicId)
                .orElseThrow(() -> new ValidationException("Không tìm thấy Topics "));

        if (!user.getLevel().getCode().equals(topic.getLevel().getCode())) {
            throw new ValidationException("Topic không phù hợp với trình độ của người học");
        }

        List<Exercises> exercises = exercisesRepository.findByTopicId(topicId);
        return exerciesMapper.toListDTO(exercises);
    }

    
    public ExercisesRespone getExerciesById(Long id) {
        Exercises exercise = exercisesRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Không tìm thấy Exercises"));
        return exerciesMapper.toDTO(exercise);
    }

  
    @Transactional
    public ExercisesRespone createExercise(ExercisesRequest req, MultipartFile audioFile) {
        Exercises.ExerciseType typeEnum = parseType(req.getType());
        Exercises exercise = buildExercise(req, typeEnum, audioFile);
        exercisesRepository.save(exercise);
        return exerciesMapper.toDTO(exercise);
    }


    @Transactional
    public ExercisesRespone updateExercise(Long id, ExercisesRequest req, MultipartFile audioFile) {
        Exercises exercise = exercisesRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Exercise không tồn tại"));

        Exercises.ExerciseType typeEnum = parseType(req.getType());
        Topics topic = topicsRepository.findById(req.getTopicId())
                .orElseThrow(() -> new ValidationException("Topic không tồn tại"));
        exercise.setTopic(topic);
        exercise.setTitle(req.getTitle());
        exercise.setType(typeEnum);
        exercise.setQuestions(req.getQuestions());
        exercise.setDuration(req.getDuration());
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

    
    private Exercises.ExerciseType parseType(String type) {
        try {
            return Exercises.ExerciseType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Type không hợp lệ: " + type);
        }
    }

    private Exercises buildExercise(ExercisesRequest req, Exercises.ExerciseType typeEnum, MultipartFile audioFile) {
        Topics topic = topicsRepository.findById(req.getTopicId())
                .orElseThrow(() -> new ValidationException("Topic không tồn tại"));

        Exercises exercise = new Exercises();
        exercise.setTopic(topic);
        exercise.setTitle(req.getTitle());
        exercise.setType(typeEnum);
        exercise.setQuestions(req.getQuestions());
        exercise.setDuration(req.getDuration());

        handleAudioUpload(exercise, typeEnum, audioFile);
        return exercise;
    }

    private void handleAudioUpload(Exercises exercise, Exercises.ExerciseType typeEnum, MultipartFile audioFile) {
        if (isAudioExercise(typeEnum)) {
            if (audioFile == null || audioFile.isEmpty()) {
                throw new ValidationException("Bài nghe yêu cầu upload audio");
            }
            String audioUrl = cloudinaryService.uploadAudio(audioFile);
            exercise.setAudioUrl(audioUrl);
        } else {
            exercise.setAudioUrl(null);
        }
    }

    private boolean isAudioExercise(Exercises.ExerciseType type) {
        switch (type) {
            case LISTEN_WRITE:
            case LISTEN_QA:
            case LISTEN_CHOICE:
            case LISTEN_FILL:
                return true;
            default:
                return false;
        }
    }
}

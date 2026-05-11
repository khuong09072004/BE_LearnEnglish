package com.learnenglish.LearnEnglish.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.learnenglish.LearnEnglish.dto.requests.ExerciseItemRequest;
import com.learnenglish.LearnEnglish.dto.responses.ExerciseItemResponse;
import com.learnenglish.LearnEnglish.entity.ExerciseItems;
import com.learnenglish.LearnEnglish.entity.Exercises;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.exception.ValidationException;
import com.learnenglish.LearnEnglish.mapper.ExerciseItemMapper;
import com.learnenglish.LearnEnglish.repository.ExerciseItemsRepository;
import com.learnenglish.LearnEnglish.repository.ExercisesRepository;
import com.learnenglish.LearnEnglish.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExerciseItemsService {

    private final ExerciseItemsRepository itemsRepo;
    private final ExercisesRepository exercisesRepo;
    private final ExerciseItemMapper mapper;
    private final UserRepository userRepository;

    
    @Transactional
    public ExerciseItemResponse create(ExerciseItemRequest req) {
        validateRequest(req);

        Exercises exercise = exercisesRepo.findById(req.getExerciseId())
                .orElseThrow(() -> new ValidationException("Exercise không tồn tại"));

        ExerciseItems item = new ExerciseItems();
        item.setExercise(exercise);
        item.setPosition(req.getPosition());
        item.setQuestionJson(req.getQuestion());
        item.setAnswerJson(req.getAnswer());

        itemsRepo.save(item);

        return mapper.toDTO(item, true); 
    }

    @Transactional
    public List<ExerciseItemResponse> createBulk(List<ExerciseItemRequest> reqs) {
        if (reqs == null || reqs.isEmpty()) {
            throw new ValidationException("Danh sách câu hỏi không được để trống");
        }

        Set<Long> exerciseIds = new HashSet<>();
        for (ExerciseItemRequest req : reqs) {
            validateRequest(req);
            exerciseIds.add(req.getExerciseId());
        }

        Map<Long, Exercises> exerciseMap = exercisesRepo.findAllById(exerciseIds)
                .stream()
                .collect(Collectors.toMap(Exercises::getId, e -> e));

        if (exerciseMap.size() != exerciseIds.size()) {
            Set<Long> missingIds = new HashSet<>(exerciseIds);
            missingIds.removeAll(exerciseMap.keySet());
            throw new ValidationException("Exercise không tồn tại: " + missingIds);
        }

        List<ExerciseItems> entities = new ArrayList<>();
        for (ExerciseItemRequest req : reqs) {
            ExerciseItems item = new ExerciseItems();
            item.setExercise(exerciseMap.get(req.getExerciseId()));
            item.setPosition(req.getPosition());
            item.setQuestionJson(req.getQuestion());
            item.setAnswerJson(req.getAnswer());
            entities.add(item);
        }

        List<ExerciseItems> savedItems = itemsRepo.saveAll(entities);
        return savedItems.stream()
                .map(item -> mapper.toDTO(item, true))
                .collect(Collectors.toList());
    }

    
    public ExerciseItemResponse getById(Long id, String userEmail) {
        ExerciseItems item = itemsRepo.findById(id)
                .orElseThrow(() -> new ValidationException("Item không tồn tại"));

        boolean includeAnswer = isAdmin(userEmail);

        return mapper.toDTO(item, includeAnswer);
    }

   
    public Object getByExercise(Long exerciseId, String userEmail) {
        boolean includeAnswer = isAdmin(userEmail);
        Exercises exercises=exercisesRepo.findById(exerciseId).orElseThrow(()->new ValidationException("Không tìm thấy bài tập"));
        List<ExerciseItemResponse> lst=itemsRepo.findByExerciseIdOrderByPosition(exerciseId)
                .stream()
                .map(i -> mapper.toDTO(i, includeAnswer))
                .collect(Collectors.toList());
        Map<String,Object> respones= new HashMap<>();;
        respones.put("type", exercises.getType());
        respones.put("passageId", exercises.getPassingId());
        respones.put("title", exercises.getTitle());
        respones.put("duration",exercises.getDuration());
        respones.put("audioUrl", exercises.getAudioUrl());
        respones.put("ExerciesItem",lst);
        return respones;
    }


    @Transactional
    public ExerciseItemResponse update(Long id, ExerciseItemRequest req) {
        ExerciseItems item = itemsRepo.findById(id)
                .orElseThrow(() -> new ValidationException("Item không tồn tại"));

        item.setPosition(req.getPosition());
        item.setQuestionJson(req.getQuestion());
        item.setAnswerJson(req.getAnswer());

        itemsRepo.save(item);

        return mapper.toDTO(item, true); 
    }

    @Transactional
    public void delete(Long id) {
        ExerciseItems item = itemsRepo.findById(id)
                .orElseThrow(() -> new ValidationException("Item không tồn tại"));

        itemsRepo.delete(item);
    }

    private boolean isAdmin(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Không tìm thấy tài khoản"));

        return user.getRole().equals(User.Role.ADMIN);
    }

    private void validateRequest(ExerciseItemRequest req) {
        if (req == null) {
            throw new ValidationException("Dữ liệu câu hỏi không hợp lệ");
        }
        if (req.getExerciseId() == null) {
            throw new ValidationException("exerciseId không được để trống");
        }
        if (req.getPosition() <= 0) {
            throw new ValidationException("position phải lớn hơn 0");
        }
        if (req.getQuestion() == null) {
            throw new ValidationException("question không được để trống");
        }
        if (req.getAnswer() == null) {
            throw new ValidationException("answer không được để trống");
        }
    }
}

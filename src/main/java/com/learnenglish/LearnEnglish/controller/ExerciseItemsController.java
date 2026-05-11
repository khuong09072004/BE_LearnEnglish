package com.learnenglish.LearnEnglish.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.learnenglish.LearnEnglish.dto.ApiResponse;
import com.learnenglish.LearnEnglish.dto.requests.ExerciseItemRequest;
import com.learnenglish.LearnEnglish.dto.responses.ExerciseItemResponse;
import com.learnenglish.LearnEnglish.service.ExerciseItemsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/exercise-items")
@RequiredArgsConstructor
public class ExerciseItemsController {

    private final ExerciseItemsService service;

    @PostMapping
    public ApiResponse<ExerciseItemResponse> create(
            @RequestBody ExerciseItemRequest req
    ) {
        ExerciseItemResponse item = service.create(req);
        return ApiResponse.success("Tạo câu hỏi thành công", item);
    }

    @PostMapping("/bulk")
    public ApiResponse<List<ExerciseItemResponse>> createBulk(
            @RequestBody List<ExerciseItemRequest> reqs
    ) {
        List<ExerciseItemResponse> items = service.createBulk(reqs);
        return ApiResponse.success("Tạo nhiều câu hỏi thành công", items);
    }

    
    @GetMapping("/{id}")
    public ApiResponse<ExerciseItemResponse> getById(
            @PathVariable Long id,
            Authentication authentication
    ) {
        ExerciseItemResponse item = service.getById(id, authentication.getName());
        return ApiResponse.success("Lấy câu hỏi thành công", item);
    }


    @GetMapping("/exercise/{exerciseId}")
    public ApiResponse<?> getByExercise(
            @PathVariable Long exerciseId,
            Authentication authentication
    ) {
        Object items = service.getByExercise(exerciseId, authentication.getName());
        return ApiResponse.success("Lấy danh sách câu hỏi thành công", items);
    }

    @PutMapping("/{id}")
    public ApiResponse<ExerciseItemResponse> update(
            @PathVariable Long id,
            @RequestBody ExerciseItemRequest req
    ) {
        ExerciseItemResponse item = service.update(id, req);
        return ApiResponse.success("Cập nhật câu hỏi thành công", item);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.success("Xóa câu hỏi thành công", "DELETED");
    }
}

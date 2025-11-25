package com.learnenglish.LearnEnglish.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learnenglish.LearnEnglish.dto.ApiResponse;
import com.learnenglish.LearnEnglish.dto.responses.ExercisesRespone;
import com.learnenglish.LearnEnglish.service.ExercisesService;

@RestController
@RequestMapping("/api/exercies")
public class ExerciesController {
    @Autowired
    ExercisesService exercisesService;
    @GetMapping("")
    public ApiResponse<?> getExercises(Authentication authentication,@RequestParam Long TopicId)
    {
        Object respone=exercisesService.getExercies(authentication.getName(), TopicId);
        return ApiResponse.success("Lấy danh sách bài tập theo topic", respone);
    }

    @GetMapping("/{topicId}/category")
    public ApiResponse<?> getExercisesByCategory(Authentication authentication,
            @PathVariable Long topicId,
            @RequestParam String category) {
                 Object respone =exercisesService.getExercisesByTopicAndCategory(authentication.getName(), topicId, category);
        return ApiResponse.success("Danh sách câu hỏi trong category ", respone);
    }


    @GetMapping("{id}")
    public ApiResponse<?> getExerciseById(Authentication authentication,@PathVariable Long id)
    {
        Object respone=exercisesService.getExerciesById(id);
        return ApiResponse.success("Lấy chi tiết bài tập", respone);
    }
}

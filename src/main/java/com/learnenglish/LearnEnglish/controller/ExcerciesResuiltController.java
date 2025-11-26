package com.learnenglish.LearnEnglish.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learnenglish.LearnEnglish.dto.ApiResponse;
import com.learnenglish.LearnEnglish.dto.requests.ExerciseSubmitRequest;
import com.learnenglish.LearnEnglish.service.ExerciseResultService;



@RestController
@RequestMapping("/api/results")
public class ExcerciesResuiltController {
    @Autowired
    ExerciseResultService exerciseResultService;
    @PostMapping("/vocab")
    public ApiResponse<?> getExercises(Authentication authentication,@RequestBody ExerciseSubmitRequest request)
    {
        Object respone=exerciseResultService.gradeVocabExercise(request,authentication.getName());
        return ApiResponse.success("Kết quả bài tập", respone);
    }
}

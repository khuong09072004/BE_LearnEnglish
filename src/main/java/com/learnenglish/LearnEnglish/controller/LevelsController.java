package com.learnenglish.LearnEnglish.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learnenglish.LearnEnglish.dto.ApiResponse;
import com.learnenglish.LearnEnglish.service.LevelsService;

@RestController
@RequestMapping("api/levels")
public class LevelsController {
    @Autowired
    LevelsService levelsService;

    @GetMapping("")
    public ApiResponse<?> getLevels()
    {
        Object repone=levelsService.getLevels();
        return ApiResponse.success("Danh sách level", repone);
    }
}

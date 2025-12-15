package com.learnenglish.LearnEnglish.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learnenglish.LearnEnglish.dto.ApiResponse;
import com.learnenglish.LearnEnglish.service.LevelsService;

import io.swagger.v3.oas.annotations.Operation;

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

    @GetMapping("/available")
    @Operation(summary = "Lấy danh sách level có thể chọn cho user")
    public ApiResponse<?> getAvailableLevelsForUser(Authentication authentication)
    {
        Object repone=levelsService.getAvailableLevelsForUser(authentication);
        return ApiResponse.success("Danh sách level", repone);
    }

    @PostMapping("/select")
    @Operation(summary = "Chọn level cho user")
    public ApiResponse<?> selectLevelForUser (Authentication authentication,@RequestParam String code)
    {
        levelsService.selectLevel(authentication.getName(),code);
        return ApiResponse.success("Chọn level thành công", null);
    }
}

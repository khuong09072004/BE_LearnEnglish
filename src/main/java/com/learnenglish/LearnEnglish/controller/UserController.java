package com.learnenglish.LearnEnglish.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learnenglish.LearnEnglish.dto.ApiResponse;
import com.learnenglish.LearnEnglish.dto.requests.SelectLevelRequest;
import com.learnenglish.LearnEnglish.dto.responses.TopicsRespone;
import com.learnenglish.LearnEnglish.entity.Topics;
import com.learnenglish.LearnEnglish.service.TopicsService;
import com.learnenglish.LearnEnglish.service.UserService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
   
    @Autowired
    UserService userService;

    @Autowired
    TopicsService topicsService;

    @PutMapping("/level")
    @Operation(summary = "Chọn trình độ cho user khi mới đăng ký")
    public ApiResponse<?>  selectLevel(@RequestBody SelectLevelRequest request,Authentication authentication)
    {
        userService.selectLevel(request.getLevel(),authentication.getName());
        return ApiResponse.success("Chọn trình độ thành công", null);
    }

    @GetMapping("/Topics")
    @Operation(summary = "Lấy danh sách chủ đề theo level của user")
    public ApiResponse<?> getTopics(Authentication authentication)
    {
        List<TopicsRespone> lstTopics= topicsService.getTopics(authentication.getName());
        return ApiResponse.success("success", lstTopics);
    }
}

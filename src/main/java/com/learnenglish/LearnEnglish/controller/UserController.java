package com.learnenglish.LearnEnglish.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.learnenglish.LearnEnglish.dto.ApiResponse;
import com.learnenglish.LearnEnglish.dto.requests.SelectLevelRequest;
import com.learnenglish.LearnEnglish.dto.requests.UpdatePasswordRequest;
import com.learnenglish.LearnEnglish.dto.requests.UpdateProfileRequest;
import com.learnenglish.LearnEnglish.dto.responses.LevelProgressDetailResponse;
import com.learnenglish.LearnEnglish.dto.responses.StudyHistoryDto;
import com.learnenglish.LearnEnglish.dto.responses.TopicProgressDto;
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

    @GetMapping("/Tracking/level")
    @Operation(summary = "Lấy tiến độ của User theo level đang làm")
    public ApiResponse<?> getLevelProgress(Authentication authentication) {
        return ApiResponse.success("success", userService.getLevelProgress(authentication.getName()));
    }

    @GetMapping("/Tracking/level/detail")
    @Operation(summary = "Lấy chi tiết tiến độ theo level với breakdown per topic và lịch sử 7 ngày")
    public ApiResponse<?> getLevelProgressDetail(Authentication authentication) {
        LevelProgressDetailResponse detail = userService.getLevelProgressDetail(authentication.getName());
        return ApiResponse.success("success", detail);
    }

    @GetMapping("/Tracking/level/history")
    @Operation(summary = "Lấy lịch sử học tập theo khoảng thời gian (dạng time-series cho chart)")
    public ApiResponse<?> getLevelProgressHistory(
            Authentication authentication,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false, defaultValue = "day") String granularity) {
        
        if (startDate == null) {
            startDate = LocalDate.now().minusDays(6);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        
        StudyHistoryDto history = userService.getLevelProgressHistory(authentication.getName(), startDate, endDate, granularity);
        return ApiResponse.success("success", history);
    }

    @GetMapping("/Tracking/topic/{topicId}")
    @Operation(summary = "Lấy chi tiết tiến độ của một topic cụ thể")
    public ApiResponse<?> getTopicProgress(
            Authentication authentication,
            @PathVariable Long topicId) {
        TopicProgressDto topicProgress = userService.getTopicProgress(authentication.getName(), topicId);
        return ApiResponse.success("success", topicProgress);
    }

    @GetMapping("/Profile")
    @Operation(summary = "Lấy thông tin người dùng")
    public ApiResponse<?> getProfile(Authentication authentication) {
        return ApiResponse.success("success", userService.getProfile(authentication.getName()));
    }
    
    @PutMapping("/profile")
     @Operation(summary = "Cập nhật thông tin người dùng")
    public ApiResponse<?> updateProfile(
            Authentication authentication,
            @RequestBody UpdateProfileRequest request) {

        Object response=userService.updateProfile(authentication.getName(), request);
        return ApiResponse.success("Cập nhật thông tin thành công", response);
    }

    @PostMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Cập nhật avatar người dùng")
    public ApiResponse<?> updateAvatar(
            Authentication authentication,
            @RequestParam("avatar") MultipartFile avatarFile) {

        String avatarUrl = userService.updateAvatar(authentication.getName(), avatarFile);
        return ApiResponse.success("Cập nhật avatar thành công", avatarUrl);
    }
    
    @PutMapping("/password")
    @Operation(summary = "Cập nhật mật khẩu người dùng")
    public ApiResponse<?> updatePassword(
            Authentication authentication,
            @RequestBody UpdatePasswordRequest request) {

        userService.changePassword(authentication.getName(), request);
        return ApiResponse.success("Cập nhật mật khẩu thành công", null);
    }
}
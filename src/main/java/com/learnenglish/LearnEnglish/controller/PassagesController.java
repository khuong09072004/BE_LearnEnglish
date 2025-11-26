package com.learnenglish.LearnEnglish.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learnenglish.LearnEnglish.dto.ApiResponse;
import com.learnenglish.LearnEnglish.service.PassagesService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/passages")
@RequiredArgsConstructor
public class PassagesController {
    @Autowired
    PassagesService passagesService;
    @GetMapping("/{id}")
    @Operation(summary = "Lấy bài đọc theo id)")
    public ApiResponse<?> getPassagesById(@PathVariable Long id) {
        Object respone = passagesService.getPassagesById(id);
        return ApiResponse.success("Chi tiết passages ", respone);
    }
}

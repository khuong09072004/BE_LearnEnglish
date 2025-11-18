package com.learnenglish.LearnEnglish.controller;

import java.nio.file.attribute.UserPrincipal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learnenglish.LearnEnglish.dto.ApiResponse;
@RestController
@RequestMapping("api/")
public class AdminController {
    @GetMapping("/admin/test")
    public ApiResponse<?> adminTest() {
        return ApiResponse.success("Admin API accessed", "Hello " );
    }
}

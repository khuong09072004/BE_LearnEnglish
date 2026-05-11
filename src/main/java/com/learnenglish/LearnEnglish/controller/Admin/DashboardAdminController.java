package com.learnenglish.LearnEnglish.controller.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learnenglish.LearnEnglish.dto.ApiResponse;
import com.learnenglish.LearnEnglish.service.AdminService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/admin/dashboard")
public class DashboardAdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    @Operation(summary = "Tổng quan dashboard admin")
    public ApiResponse<?> getDashboardOverview() {
        return ApiResponse.success("Dashboard overview", adminService.getDashboardOverview());
    }
}
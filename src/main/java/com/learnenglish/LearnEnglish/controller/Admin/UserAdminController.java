package com.learnenglish.LearnEnglish.controller.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.learnenglish.LearnEnglish.dto.ApiResponse;
import com.learnenglish.LearnEnglish.dto.requests.UserStatusRequest;
import com.learnenglish.LearnEnglish.service.AdminService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("api/admin/users")

public class UserAdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping
    @Operation(summary = "Danh sách tất cả người dùng (Admin)")
    public ApiResponse<?> getAllUsers() {
        return ApiResponse.success("Danh sách người dùng", adminService.getAllUsers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Chi tiết người dùng (Admin)")
    public ApiResponse<?> getUserById(@PathVariable Long id) {
        return ApiResponse.success("Chi tiết người dùng", adminService.getUserById(id));
    }

    // ĐÃ SỬA CHỈ CÒN STATUS
    @PutMapping("/{id}/status")
    @Operation(summary = "Khóa hoặc Mở khóa tài khoản (Admin)")
    public ApiResponse<?> updateUserStatus(@PathVariable Long id, @RequestBody UserStatusRequest request) {
        return ApiResponse.success("Cập nhật trạng thái thành công", adminService.updateUserStatus(id, request));
    }
}

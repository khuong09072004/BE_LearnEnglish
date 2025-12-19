package com.learnenglish.LearnEnglish.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import com.learnenglish.LearnEnglish.dto.ApiResponse;
import com.learnenglish.LearnEnglish.dto.responses.NotificationResponse;
import com.learnenglish.LearnEnglish.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ApiResponse<Page<NotificationResponse>> getMyNotifications(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.success(
            "Lấy danh sách thông báo của tôi",
            notificationService.getMyNotifications(
                PageRequest.of(page, size)
            )
        );
    }

    @PostMapping("/{id}/read")
    public ApiResponse<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ApiResponse.success("Thông báo đã được đánh dấu là đã đọc", null);
    }

  
    @PostMapping("/read-all")
    public ApiResponse<Void> markAllAsRead() {
        notificationService.markAllAsRead();
        return ApiResponse.success("Đã đánh dấu tất cả thông báo là đã đọc", null);
    }

    @GetMapping("/unread-count")
    public ApiResponse<Long> getUnreadCount() {
        return ApiResponse.success(
            "Lấy số lượng thông báo chưa đọc ",
            notificationService.getUnreadCount()
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteOne(@PathVariable Long id) {
        notificationService.deleteById(id);
        return ApiResponse.success("Đã xóa notification", null);
    }

    @DeleteMapping
    public ApiResponse<?> deleteAll() {
        notificationService.deleteAll();
        return ApiResponse.success("Đã xóa tất cả notification", null);
    }
}

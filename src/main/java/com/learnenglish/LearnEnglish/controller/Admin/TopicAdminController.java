package com.learnenglish.LearnEnglish.controller.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.learnenglish.LearnEnglish.dto.ApiResponse;
import com.learnenglish.LearnEnglish.dto.requests.TopicRequest;
import com.learnenglish.LearnEnglish.service.AdminService;
import com.learnenglish.LearnEnglish.service.TopicsService;

@RestController
@RequestMapping("api/admin/topics")
public class TopicAdminController {
    @Autowired
    private TopicsService topicsService;
    @Autowired
    private AdminService adminService;

    @GetMapping
    public ApiResponse<?> getTopics(@RequestParam Long levelId) {
        return ApiResponse.success("Danh sách topics", adminService.getTopics(levelId));
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getTopicById(@PathVariable Long id) {
        return ApiResponse.success("Chi tiết topic", adminService.getTopicsById(id));
    }

    @PostMapping
    public ApiResponse<?> createTopic(@RequestBody TopicRequest request) {
        return ApiResponse.success("Thêm topic thành công", topicsService.createTopics(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<?> updateTopic(@PathVariable Long id, @RequestBody TopicRequest request) {
        return ApiResponse.success("Cập nhật topic thành công", topicsService.updateTopics(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteTopic(@PathVariable Long id) {
        topicsService.deleteTopics(id);
        return ApiResponse.success("Xóa topic thành công", null);
    }
}

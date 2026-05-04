package com.learnenglish.LearnEnglish.controller.Admin;

import java.util.List;

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
import com.learnenglish.LearnEnglish.dto.requests.ConversationLessonRequest;
import com.learnenglish.LearnEnglish.dto.requests.ConversationStepRequest;
import com.learnenglish.LearnEnglish.dto.responses.ConversationSessionResponse;
import com.learnenglish.LearnEnglish.service.ConversationService;
import java.util.List;

@RestController
@RequestMapping("/api/admin/conversations")
public class ConversationAdminController {

    @Autowired
    private ConversationService conversationService;

    @GetMapping("/lessons")
    public ApiResponse<?> getLessons() {
        return ApiResponse.success("Danh sách conversation lessons", conversationService.getAllLessons());
    }

    @GetMapping("/lessons/{lessonId}")
    public ApiResponse<?> getLessonById(@PathVariable Long lessonId) {
        return ApiResponse.success("Chi tiết conversation lesson", conversationService.getLesson(lessonId));
    }

    @PostMapping("/lessons")
    public ApiResponse<?> createLesson(@RequestBody ConversationLessonRequest request) {
        return ApiResponse.success("Tạo conversation lesson thành công", conversationService.createLesson(request));
    }

    @PutMapping("/lessons/{lessonId}")
    public ApiResponse<?> updateLesson(@PathVariable Long lessonId, @RequestBody ConversationLessonRequest request) {
        return ApiResponse.success("Cập nhật conversation lesson thành công", conversationService.updateLesson(lessonId, request));
    }

    @DeleteMapping("/lessons/{lessonId}")
    public ApiResponse<?> deleteLesson(@PathVariable Long lessonId) {
        conversationService.deleteLesson(lessonId);
        return ApiResponse.success("Xóa conversation lesson thành công", null);
    }

    @GetMapping("/lessons/{lessonId}/steps")
    public ApiResponse<?> getStepsByLesson(@PathVariable Long lessonId) {
        return ApiResponse.success("Danh sách step theo lesson", conversationService.getStepsByLesson(lessonId));
    }

    @GetMapping("/lessons/{lessonId}/steps/{stepId}")
    public ApiResponse<?> getStepById(
            @PathVariable Long lessonId,
            @PathVariable Long stepId) {
        return ApiResponse.success("Chi tiết step", conversationService.getStepByLessonAndId(lessonId, stepId));
    }

    @PostMapping("/lessons/{lessonId}/steps")
    public ApiResponse<?> createStep(
            @PathVariable Long lessonId,
            @RequestBody ConversationStepRequest request) {
        return ApiResponse.success("Tạo step thành công", conversationService.createStep(lessonId, request));
    }

    @PutMapping("/lessons/{lessonId}/steps/{stepId}")
    public ApiResponse<?> updateStep(
            @PathVariable Long lessonId,
            @PathVariable Long stepId,
            @RequestBody ConversationStepRequest request) {
        return ApiResponse.success("Cập nhật step thành công", conversationService.updateStep(lessonId, stepId, request));
    }

    @DeleteMapping("/lessons/{lessonId}/steps/{stepId}")
    public ApiResponse<?> deleteStep(
            @PathVariable Long lessonId,
            @PathVariable Long stepId) {
        conversationService.deleteStep(lessonId, stepId);
        return ApiResponse.success("Xóa step thành công", null);
    }

    @PostMapping("/lessons/{lessonId}/steps/bulk")
    public ApiResponse<?> createStepsBulk(
            @PathVariable Long lessonId,
            @RequestBody List<ConversationStepRequest> requests) {
        return ApiResponse.success("Tạo nhiều steps thành công", conversationService.createStepsBulk(lessonId, requests));
    }

    @GetMapping("/lessons/{lessonId}/steps/suggest")
    public ApiResponse<?> suggestSteps(
            @PathVariable Long lessonId,
            @RequestParam(required = false) Integer count) {
        return ApiResponse.success("Gợi ý steps từ AI", conversationService.generateStepSuggestions(lessonId, count));
    }

    @GetMapping("/sessions")
    public ApiResponse<?> getSessions(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long lessonId) {
        List<ConversationSessionResponse> sessions = conversationService.getAllSessions();
        if (userId != null) {
            sessions = sessions.stream()
                    .filter(session -> userId.equals(session.getUserId()))
                    .toList();
        }
        if (lessonId != null) {
            sessions = sessions.stream()
                    .filter(session -> lessonId.equals(session.getLessonId()))
                    .toList();
        }
        return ApiResponse.success("Danh sách session", sessions);
    }

    @GetMapping("/sessions/{sessionId}")
    public ApiResponse<?> getSessionDetail(@PathVariable Long sessionId) {
        return ApiResponse.success("Chi tiết session", conversationService.getSessionDetail(sessionId));
    }

    @GetMapping("/sessions/{sessionId}/turns")
    public ApiResponse<?> getSessionTurns(@PathVariable Long sessionId) {
        return ApiResponse.success("Danh sách turns", conversationService.getSessionTurns(sessionId));
    }
}
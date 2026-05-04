package com.learnenglish.LearnEnglish.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learnenglish.LearnEnglish.dto.ApiResponse;
import com.learnenglish.LearnEnglish.service.ConversationService;


@RestController
@RequestMapping("/api/conversations")
public class ConversationChatController {


    @Autowired
    private ConversationService conversationService;

    @PostMapping("/start")
    public ApiResponse<?> startLesson(
            @RequestParam Long lessonId,
            @RequestParam Long userId) {
        return ApiResponse.success("Lesson started", conversationService.startLesson(lessonId, userId));
    }

    @PostMapping("/{sessionId}/reply")
    public ApiResponse<?>  reply(
            @PathVariable Long sessionId,
            @RequestBody String message) {
        return ApiResponse.success("AI Response", conversationService.reply(sessionId, message));
    }

    @GetMapping("/lessons/{lessonId}")
    public ApiResponse<?> getLesson(
            @PathVariable Long lessonId,
            @RequestParam(required = false) Long userId) {
        if (userId == null) {
            return ApiResponse.success("Lesson detail", conversationService.getLesson(lessonId));
        }
        return ApiResponse.success("Lesson detail with learning status", conversationService.getLessonForUser(lessonId, userId));
    }

    @GetMapping("/lessons")
    public ApiResponse<?> getAllLessons(@RequestParam Long userId) {
        return ApiResponse.success("All lessons with learning status", conversationService.getAllLessonsForUser(userId));
    }

    @GetMapping("/sessions/{sessionId}")
    public ApiResponse<?> getSessionDetail(@PathVariable Long sessionId) {
        return ApiResponse.success("Session detail", conversationService.getSessionDetail(sessionId));
    }

    @GetMapping("/sessions/{sessionId}/turns")
    public ApiResponse<?> getSessionTurns(@PathVariable Long sessionId) {
        return ApiResponse.success("Session turns", conversationService.getSessionTurns(sessionId));
    }

    @GetMapping("/users/{userId}/sessions")
    public ApiResponse<?> getUserSessions(@PathVariable Long userId) {
        return ApiResponse.success("User sessions", conversationService.getSessionsByUser(userId));
    }

    @GetMapping("/lessons/{lessonId}/history")
    public ApiResponse<?> getLearnedHistory(
            @PathVariable Long lessonId,
            @RequestParam Long userId) {
        return ApiResponse.success(
                "Learned conversation history",
                conversationService.getLearnedHistoryByLesson(lessonId, userId));
    }

}

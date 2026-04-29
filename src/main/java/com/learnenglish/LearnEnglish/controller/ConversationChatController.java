package com.learnenglish.LearnEnglish.controller;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnenglish.LearnEnglish.dto.ApiResponse;
import com.learnenglish.LearnEnglish.entity.ConversationTurn;
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
    public ApiResponse<?> getLesson(@PathVariable Long lessonId) {
        return ApiResponse.success("Lesson detail", conversationService.getLesson(lessonId));
    }

    @GetMapping("/lessons")
    public ApiResponse<?> getAllLessons() {
        return ApiResponse.success("All lessons", conversationService.getAllLessons());
    }

}

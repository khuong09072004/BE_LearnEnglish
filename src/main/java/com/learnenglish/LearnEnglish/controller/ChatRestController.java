package com.learnenglish.LearnEnglish.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learnenglish.LearnEnglish.dto.ApiResponse;
import com.learnenglish.LearnEnglish.dto.responses.ChatMessageResponse;
import com.learnenglish.LearnEnglish.service.ChatMessageService;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {

    @Autowired
    private ChatMessageService chatMessageService;

    @GetMapping("/topics/{topicId}/messages")
    public ApiResponse<?> getMessages(
            @PathVariable Long topicId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ApiResponse.success(
                "Lấy danh sách tin nhắn thành công",
                chatMessageService.getMessagesByTopic(topicId, page, size)
        );
    }

    @PutMapping("/messages/{messageId}")
    public ApiResponse<?> updateMessage(
            @PathVariable Long messageId,
            @RequestParam String content
    ) {
        return ApiResponse.success(
                "Cập nhật tin nhắn thành công",
                chatMessageService.updateMessage(messageId, content)
        );
    }
    @DeleteMapping("/messages/{messageId}")
    public ApiResponse<?> deleteMessage(
            @PathVariable Long messageId
    ) {
        chatMessageService.deleteMessage(messageId);
        return ApiResponse.success("Xóa tin nhắn thành công", null);
    }
}
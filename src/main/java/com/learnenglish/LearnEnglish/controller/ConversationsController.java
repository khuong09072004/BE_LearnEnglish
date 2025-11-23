package com.learnenglish.LearnEnglish.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learnenglish.LearnEnglish.dto.ApiResponse;
import com.learnenglish.LearnEnglish.service.ConversationsService;

@RestController
@RequestMapping("/api/conversations")
public class ConversationsController {
    @Autowired
    ConversationsService conversationsService;

    @GetMapping("")
    public ApiResponse<?> getConversations(Authentication authentication,@RequestParam Long TopicId)
    {
        Object respone=conversationsService.getConversations(authentication.getName(), TopicId);
        return ApiResponse.success("Lấy danh sách hội thoại theo topic", respone);
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getConversationsById(@PathVariable Long id)
    {
        Object respone=conversationsService.getConversationById(id);
        return ApiResponse.success("Chi tiết hội thoại", respone);
    }

}

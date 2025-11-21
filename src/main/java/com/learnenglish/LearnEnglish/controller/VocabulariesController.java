package com.learnenglish.LearnEnglish.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learnenglish.LearnEnglish.dto.ApiResponse;
import com.learnenglish.LearnEnglish.service.VocabulariesService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vocabularies")
@RequiredArgsConstructor
public class VocabulariesController {
    @Autowired
    VocabulariesService vocabulariesService;

   @GetMapping("/topic/{topicId}")
    public ApiResponse<?> getVocabByTopic(@PathVariable Long topicId,Authentication authentication)
    {
        Object response = vocabulariesService.getVocabularies(authentication.getName(),topicId);
        return ApiResponse.success("Success", response);
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getVocabById(@PathVariable Long id,Authentication authentication)
    {
        Object response = vocabulariesService.getVocabulariesById(id);
        return ApiResponse.success("Success", response);
    }
}

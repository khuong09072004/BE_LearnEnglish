package com.learnenglish.LearnEnglish.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.learnenglish.LearnEnglish.dto.ApiResponse;
import com.learnenglish.LearnEnglish.dto.requests.VocabularyRequest;
import com.learnenglish.LearnEnglish.service.UserVocaProgressService;
import com.learnenglish.LearnEnglish.service.VocabulariesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.MediaType;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vocabularies")
@RequiredArgsConstructor
public class VocabulariesController {
    @Autowired
    VocabulariesService vocabulariesService;
    @Autowired
    UserVocaProgressService userVocaProgressService;

    @GetMapping("/topic/{topicId}")
    public ApiResponse<?> getVocabByTopic(@PathVariable Long topicId, Authentication authentication) {
        Object response = vocabulariesService.getVocabularies(authentication.getName(), topicId);
        return ApiResponse.success("Success", response);
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getVocabById(@PathVariable Long id, Authentication authentication) {
        Object response = vocabulariesService.getVocabulariesById(authentication.getName(), id);
        return ApiResponse.success("Success", response);
    }

    @PostMapping("/{id}/learn")
    public ApiResponse<?> learnVocaByUser(@PathVariable Long id, Authentication authentication) {
        Object response = userVocaProgressService.learnVocaByUser(authentication.getName(), id);
        return ApiResponse.success("Success", response);
    }

    @PostMapping("/{id}/unlearn")
    public ApiResponse<?> unlearnVocaByUser(@PathVariable Long id, Authentication authentication) {
        Object response = userVocaProgressService.unlearnVocaByUser(authentication.getName(), id);
        return ApiResponse.success("Success", response);
    }

    @GetMapping("/statistics/{topicId}")
    public ApiResponse<?> getVocabStatisticsInTopic(@PathVariable Long topicId, Authentication authentication) {
        Object response = userVocaProgressService.getVocabStatisticsInTopic(authentication.getName(), topicId);
        return ApiResponse.success("Success", response);
    }

   
}

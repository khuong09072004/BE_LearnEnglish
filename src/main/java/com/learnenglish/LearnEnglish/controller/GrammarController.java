package com.learnenglish.LearnEnglish.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learnenglish.LearnEnglish.dto.ApiResponse;
import com.learnenglish.LearnEnglish.service.GrammarService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/grammar")
@RequiredArgsConstructor
public class GrammarController {
    @Autowired
    GrammarService grammarService;
    @GetMapping("")
    public ApiResponse<?> getGrammars(Authentication authentication)
    {
        Object response = grammarService.getGrammars(authentication.getName());
        return ApiResponse.success("Success", response);
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getGrammarById(Authentication authentication,@PathVariable Long id)
    {
        Object response=grammarService.getGrammarByid(authentication.getName(),id);
        return ApiResponse.success("Success", response);
    }
}

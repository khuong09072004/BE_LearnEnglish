package com.learnenglish.LearnEnglish.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learnenglish.LearnEnglish.dto.ApiResponse;
import com.learnenglish.LearnEnglish.dto.requests.ContactCreateRequest;
import com.learnenglish.LearnEnglish.dto.responses.ContactResponse;
import com.learnenglish.LearnEnglish.service.ContactService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    public ApiResponse<ContactResponse> submitContact(@RequestBody ContactCreateRequest req,
                                                      Authentication authentication) {
        ContactResponse result = contactService.submitContact(authentication.getName(), req);
        return ApiResponse.success("Gửi liên hệ thành công", result);
    }
}

package com.learnenglish.LearnEnglish.controller.Admin;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learnenglish.LearnEnglish.dto.ApiResponse;
import com.learnenglish.LearnEnglish.dto.responses.ContactResponse;
import com.learnenglish.LearnEnglish.service.ContactService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/contacts")
@RequiredArgsConstructor
public class ContactAdminController {

    private final ContactService contactService;

    @GetMapping
    public ApiResponse<List<ContactResponse>> getAllContacts() {
        return ApiResponse.success("Danh sách contact", contactService.getAllContacts());
    }

    @GetMapping("/{id}")
    public ApiResponse<ContactResponse> getContactById(@PathVariable Long id) {
        return ApiResponse.success("Chi tiết contact", contactService.getContactById(id));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return ApiResponse.success("Xóa contact thành công", "DELETED");
    }
}

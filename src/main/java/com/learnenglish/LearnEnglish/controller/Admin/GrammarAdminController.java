package com.learnenglish.LearnEnglish.controller.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
import com.learnenglish.LearnEnglish.dto.requests.GrammarRequest;
import com.learnenglish.LearnEnglish.service.AdminService;
import com.learnenglish.LearnEnglish.service.GrammarService;

@RestController
@RequestMapping("api/admin/grammar")
public class GrammarAdminController {
    @Autowired
    private GrammarService grammarService;
    @Autowired
    private AdminService adminService;

    @GetMapping
    public ApiResponse<?> getGrammars(Authentication authentication, @RequestParam(required = false) Long levelId) {
        if (levelId != null) {
            return ApiResponse.success("Danh sách ngữ pháp theo level", adminService.getGrammars(authentication.getName(), levelId));
        }
        return ApiResponse.success("Danh sách tất cả ngữ pháp", adminService.getAllGrammars());
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getGrammarById(Authentication authentication, @PathVariable Long id) {
        Object response = adminService.getGrammarByid(authentication.getName(), id);
        return ApiResponse.success("Chi tiết ngữ pháp", response);
    }

    @PostMapping
    public ApiResponse<?> createGrammar(@RequestBody GrammarRequest request) {
        Object response = grammarService.createGrammarByAdmin(request);
        return ApiResponse.success("Thêm ngữ pháp thành công", response);
    }

    @PutMapping("/{id}")
    public ApiResponse<?> updateGrammar(@PathVariable Long id, @RequestBody GrammarRequest request) {
        Object response = grammarService.updateGrammarByAdmin(id, request);
        return ApiResponse.success("Cập nhật ngữ pháp thành công", response);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteGrammar(@PathVariable Long id) {
        grammarService.deleteGrammarByAdmin(id);
        return ApiResponse.success("Xóa ngữ pháp thành công", null);
    }
}
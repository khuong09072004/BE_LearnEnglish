package com.learnenglish.LearnEnglish.controller.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.learnenglish.LearnEnglish.dto.ApiResponse;
import com.learnenglish.LearnEnglish.dto.requests.PassageRequest;
import com.learnenglish.LearnEnglish.entity.Passages;
import com.learnenglish.LearnEnglish.service.PassagesService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/passages")
public class PassagesAdminController {

    @Autowired
    private PassagesService passagesService;

    @GetMapping
    public ApiResponse<List<Passages>> listAll() {
        return ApiResponse.success("Danh sách passages", passagesService.listAll());
    }

    @PostMapping
    public ApiResponse<Passages> create(@RequestBody PassageRequest req) {
        Passages p = passagesService.createPassage(req.getTitle(), req.getContent(), req.getCategory());
        return ApiResponse.success("Tạo passages thành công", p);
    }

    @PutMapping("/{id}")
    public ApiResponse<Passages> update(@PathVariable Long id, @RequestBody PassageRequest req) {
        Passages p = passagesService.updatePassage(id, req.getTitle(), req.getContent(), req.getCategory());
        return ApiResponse.success("Cập nhật passages thành công", p);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable Long id) {
        passagesService.deletePassage(id);
        return ApiResponse.success("Xóa passages thành công", "DELETED");
    }
}

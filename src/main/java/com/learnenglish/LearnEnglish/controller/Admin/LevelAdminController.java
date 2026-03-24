package com.learnenglish.LearnEnglish.controller.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.learnenglish.LearnEnglish.dto.ApiResponse;
import com.learnenglish.LearnEnglish.dto.requests.LevelRequest;
import com.learnenglish.LearnEnglish.service.LevelsService;

@RestController
@RequestMapping("api/admin/levels")
public class LevelAdminController {
    @Autowired
    private LevelsService levelsService;

    @GetMapping
    public ApiResponse<?> getAllLevels() {
        return ApiResponse.success("Danh sách tất cả levels", levelsService.getAllLevels());
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getLevelById(@PathVariable Long id) {
        return ApiResponse.success("Chi tiết level", levelsService.getLevelByid(id));
    }

    @PostMapping
    public ApiResponse<?> createLevel(@RequestBody LevelRequest request) {
        return ApiResponse.success("Thêm level thành công", levelsService.createLevel(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<?> updateLevel(@PathVariable Long id, @RequestBody LevelRequest request) {
        return ApiResponse.success("Cập nhật level thành công", levelsService.updateLevelByid(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteLevel(@PathVariable Long id) {
        levelsService.deleteLevelByid(id);
        return ApiResponse.success("Xóa level thành công", null);
    }
}

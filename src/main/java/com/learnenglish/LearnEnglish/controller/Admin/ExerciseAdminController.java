package com.learnenglish.LearnEnglish.controller.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.learnenglish.LearnEnglish.dto.ApiResponse;
import com.learnenglish.LearnEnglish.dto.requests.ExercisesRequest;
import com.learnenglish.LearnEnglish.service.AdminService;
import com.learnenglish.LearnEnglish.service.ExercisesService;

@RestController
@RequestMapping("api/admin/exercises")
public class ExerciseAdminController {
    @Autowired
    private ExercisesService exercisesService;
    @Autowired
    private AdminService adminService;

   @GetMapping
    public ApiResponse<?> getExercises(@RequestParam(required = false) Long topicId) {
        if (topicId != null) {
            return ApiResponse.success("Danh sách bài tập theo chủ đề", adminService.getExercies(topicId));
        }
        return ApiResponse.success("Danh sách tất cả bài tập", adminService.getAllExercises());
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getExerciseById(@PathVariable Long id) {
        return ApiResponse.success("Chi tiết bài tập", exercisesService.getExerciesById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> createExercise(
            @RequestParam Long topicId,
            @RequestParam String title,
            @RequestParam String type,
            @RequestParam int duration,
            @RequestParam(required = false) String category,
            @RequestParam(value = "audioFile", required = false) MultipartFile audioFile
    ) throws Exception {
        ExercisesRequest req = new ExercisesRequest();
        req.setTopicId(topicId);
        req.setTitle(title);
        req.setType(type);
        req.setDuration(duration);
        req.setCategory(category);

        return ApiResponse.success("Tạo bài tập thành công", exercisesService.createExercise(req, audioFile));
    }

    // Support application/json requests (no audio file). For audio exercise types this will throw validation error.
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> createExerciseJson(@RequestBody ExercisesRequest req) throws Exception {
        return ApiResponse.success("Tạo bài tập thành công", exercisesService.createExercise(req, null));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> updateExercise(
            @PathVariable Long id,
            @RequestParam Long topicId,
            @RequestParam String title,
            @RequestParam String type,
            @RequestParam int duration,
            @RequestParam(required = false) String category,
            @RequestParam(value = "audioFile", required = false) MultipartFile audioFile
    ) throws Exception {
        ExercisesRequest req = new ExercisesRequest();
        req.setTopicId(topicId);
        req.setTitle(title);
        req.setType(type);
        req.setDuration(duration);
        req.setCategory(category);

        return ApiResponse.success("Cập nhật bài tập thành công", exercisesService.updateExercise(id, req, audioFile));
    }

    // Support application/json update (no audio file). For audio exercise types this will throw validation error.
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> updateExerciseJson(@PathVariable Long id,
                                            @RequestBody ExercisesRequest req) throws Exception {
        return ApiResponse.success("Cập nhật bài tập thành công", exercisesService.updateExercise(id, req, null));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteExercise(@PathVariable Long id) {
        exercisesService.deleteExercise(id);
        return ApiResponse.success("Xóa bài tập thành công", null);
    }
}
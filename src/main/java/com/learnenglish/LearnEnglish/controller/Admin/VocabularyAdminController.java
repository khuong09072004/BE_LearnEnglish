package com.learnenglish.LearnEnglish.controller.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.learnenglish.LearnEnglish.dto.ApiResponse;
import com.learnenglish.LearnEnglish.dto.requests.VocabularyRequest;
import com.learnenglish.LearnEnglish.service.AdminService;
import com.learnenglish.LearnEnglish.service.VocabulariesService;

@RestController
@RequestMapping("api/admin/vocabularies")
public class VocabularyAdminController {
    @Autowired
    private VocabulariesService vocabulariesService;
    @Autowired
    private AdminService adminService;

    @GetMapping
    public ApiResponse<?> getVocabularies(Authentication authentication, @RequestParam Long topicId) {
        Object response = adminService.getVocabularies(authentication.getName(), topicId);
        return ApiResponse.success("Danh sách từ vựng theo chủ đề", response);
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getVocabById(@PathVariable Long id, Authentication authentication) {
        Object response = adminService.getVocabulariesById(authentication.getName(), id);
        return ApiResponse.success("Chi tiết từ vựng", response);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> createVocab(@ModelAttribute VocabularyRequest req, 
                                     @RequestParam(required = false) MultipartFile imageFile) {
        Object response = vocabulariesService.createVocabularyByAdmin(req, imageFile);
        return ApiResponse.success("Thêm từ vựng thành công", response);
    }

    @PutMapping(value = "/{vocabId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> updateVocab(@PathVariable Long vocabId, 
                                     @ModelAttribute VocabularyRequest req,
                                     @RequestParam(required = false) MultipartFile imageFile) {
        Object response = vocabulariesService.updateVocabularyByAdmin(vocabId, req, imageFile);
        return ApiResponse.success("Cập nhật từ vựng thành công", response);
    }

    @DeleteMapping("/{vocabId}")
    public ApiResponse<?> deleteVocab(@PathVariable Long vocabId) {
        vocabulariesService.deleteVocabularyByAdmin(vocabId);
        return ApiResponse.success("Xóa từ vựng thành công", null);
    }
}

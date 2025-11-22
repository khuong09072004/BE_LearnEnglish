package com.learnenglish.LearnEnglish.controller;

import java.nio.file.attribute.UserPrincipal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.learnenglish.LearnEnglish.dto.ApiResponse;
import com.learnenglish.LearnEnglish.dto.requests.GrammarRequest;
import com.learnenglish.LearnEnglish.dto.requests.LevelRequest;
import com.learnenglish.LearnEnglish.dto.requests.VocabularyRequest;
import com.learnenglish.LearnEnglish.service.AdminService;
import com.learnenglish.LearnEnglish.service.GrammarService;
import com.learnenglish.LearnEnglish.service.LevelsService;
import com.learnenglish.LearnEnglish.service.VocabulariesService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("api/admin")
public class AdminController {
    @Autowired
    VocabulariesService vocabulariesService;
    @Autowired
    GrammarService grammarService;
    @Autowired
    AdminService adminService;
    @Autowired
    LevelsService levelsService;
    //Vocabulary
    @GetMapping("/vocabularies")
    @Operation(summary = "Danh sách từ vựng theo Topic  (Admin)")
    public ApiResponse<?> getVocabularies(Authentication authentication,@RequestParam Long topicId)
    {
         Object response = adminService.getVocabularies(authentication.getName(), topicId);
         return ApiResponse.success("Danh sách từ vựng theo chủ đề", response);
    }

    @GetMapping("/vocabularies/{id}")
    @Operation(summary = "Chi tiết từ vựng  (Admin)")
    public ApiResponse<?> getVocabById(@PathVariable Long id, Authentication authentication) {
        Object response = adminService.getVocabulariesById(authentication.getName(), id);
        return ApiResponse.success("Success", response);
    }

    @PostMapping(value = "/vocabularies", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Thêm từ vựng  (Admin)", description = "Create")
    public ApiResponse<?> creatVocabByAdmin(
            @RequestParam Long topicId,
            @RequestParam String word,
            @RequestParam String meaning,
            @RequestParam(required = false) String phonetic,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) MultipartFile imageFile) {

        VocabularyRequest req = new VocabularyRequest(topicId, word, meaning, phonetic, description);

        Object response = vocabulariesService.createVocabularyByAdmin(req, imageFile);

        return ApiResponse.success("Tạo từ vựng mới thành công", response);
    }

    @PutMapping(value = "/vocabularies/{vocabId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Cập nhật từ vựng  (Admin)", description = "Update")
    public ApiResponse<?> updateVocabByAdmin( @PathVariable Long vocabId,
            @RequestParam Long topicId,
            @RequestParam String word,
            @RequestParam String meaning,
            @RequestParam(required = false) String phonetic,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) MultipartFile imageFile) {
        VocabularyRequest req = new VocabularyRequest(topicId, word, meaning, phonetic, description);
        Object response = vocabulariesService.updateVocabularyByAdmin(vocabId, req,
                imageFile);
        return ApiResponse.success("Cập nhật từ vựng thành công", response);
    }

    @DeleteMapping(value = "/vocabularies/{vocabId}")
    @Operation(summary = "xóa từ vựng  (Admin)", description = "Delete")
    public ApiResponse<?> deleteVocabByAdmin(@PathVariable Long vocabId) {
        Object response = vocabulariesService.deleteVocabularyByAdmin(vocabId);
        return ApiResponse.success("Xóa từ vựng thành công", response);
    }

    //Grammar
    @GetMapping("/grammar")
    @Operation(summary = "Danh sách ngữ pháp (Admin) theo level")
    public ApiResponse<?> getGrammars(Authentication authentication,@RequestParam Long levelId)
    {
         Object response = adminService.getGrammars(authentication.getName(), levelId);
         return ApiResponse.success("Danh sách từ vựng theo chủ đề", response);
    }

    @GetMapping("/grammar/{id}")
    @Operation(summary = "Chi tiết ngữ pháp (Admin) ")
    public ApiResponse<?> getGrammarById(Authentication authentication,@PathVariable Long id)
    {
        Object response=adminService.getGrammarByid(authentication.getName(),id);
        return ApiResponse.success("Success", response);
    }

    @PostMapping("/grammar")
    @Operation(summary = "thêm ngữ pháp (Admin)")
    public ApiResponse<?> createGrammarByAdmin(@RequestBody GrammarRequest request) {
        Object response = grammarService.createGrammarByAdmin(request);
        return ApiResponse.success("Thêm ngữ pháp thành công", response);
    }

    @PutMapping("/grammar/{id}")
    @Operation(summary = "Cập nhật ngữ pháp (Admin)")
    public ApiResponse<?> updateGrammarByAdmin(@PathVariable Long id,@RequestBody GrammarRequest request) {
        Object response = grammarService.updateGrammarByAdmin(id,request);
        return ApiResponse.success("Cập nhật ngữ pháp thành công", response);
    }

    @DeleteMapping("/grammar/{id}")
    @Operation(summary = "xóa ngữ pháp (Admin)")
    public ApiResponse<?> updateGrammarByAdmin(@PathVariable Long id) {
        Object response = grammarService.deleteGrammarByAdmin(id);
        return ApiResponse.success("Xóa ngữ pháp thành công", response);
    }

    //level
    @GetMapping("/levels/{id}")
    @Operation(summary = "Chi tiết  level (Admin)")
    public ApiResponse<?> getLevelById(@PathVariable Long id) {
            Object response = levelsService.getLevelByid(id);
            return ApiResponse.success("Chi tiết level", response);
    }

    @PostMapping("/levels")
    @Operation(summary = "Thêm  level (Admin)")
    public ApiResponse<?> creatLevels(@RequestBody LevelRequest request) {
        Object response = levelsService.createLevel(request);
        return ApiResponse.success("Tạo level thành công", response);
    }

    @PutMapping("/levels")
    @Operation(summary = "Cập nhật  level (Admin)")
    public ApiResponse<?> updateLevels(@PathVariable Long id,@RequestBody LevelRequest request) {
        Object response = levelsService.updateLevelByid(id,request);
        return ApiResponse.success("Cập nhật level thành công", response);
    }

    @DeleteMapping("/levels")
    @Operation(summary = "Xóa  level (Admin)")
    public ApiResponse<?> deleteLevels(@PathVariable Long id) {
        Object response = levelsService.deleteLevelByid(id);
        return ApiResponse.success("Xóa level thành công", response);
    }

    //topic
    
    //conversation

    //exercies
}

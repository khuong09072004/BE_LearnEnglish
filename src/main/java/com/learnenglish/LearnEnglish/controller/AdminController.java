package com.learnenglish.LearnEnglish.controller;

import java.nio.file.attribute.UserPrincipal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnenglish.LearnEnglish.dto.ApiResponse;
import com.learnenglish.LearnEnglish.dto.requests.ConversationRequest;
import com.learnenglish.LearnEnglish.dto.requests.ExercisesRequest;
import com.learnenglish.LearnEnglish.dto.requests.GrammarRequest;
import com.learnenglish.LearnEnglish.dto.requests.LevelRequest;
import com.learnenglish.LearnEnglish.dto.requests.TopicRequest;
import com.learnenglish.LearnEnglish.dto.requests.VocabularyRequest;
import com.learnenglish.LearnEnglish.service.AdminService;
import com.learnenglish.LearnEnglish.service.ConversationsService;
import com.learnenglish.LearnEnglish.service.ExercisesService;
import com.learnenglish.LearnEnglish.service.GrammarService;
import com.learnenglish.LearnEnglish.service.LevelsService;
import com.learnenglish.LearnEnglish.service.TopicsService;
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
    @Autowired
    TopicsService topicsService;
    @Autowired
    ConversationsService conversationsService;
    @Autowired
    ExercisesService exercisesService;

    // Vocabulary
    @GetMapping("/vocabularies")
    @Operation(summary = "Danh sách từ vựng theo Topic  (Admin)")
    public ApiResponse<?> getVocabularies(Authentication authentication, @RequestParam Long topicId) {
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

        return ApiResponse.success("Thêm mới từ vựng mới thành công", response);
    }

    @PutMapping(value = "/vocabularies/{vocabId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Cập nhật từ vựng  (Admin)", description = "Update")
    public ApiResponse<?> updateVocabByAdmin(@PathVariable Long vocabId,
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

    // Grammar
    @GetMapping("/grammar")
    @Operation(summary = "Danh sách ngữ pháp (Admin) theo level")
    public ApiResponse<?> getGrammars(Authentication authentication, @RequestParam Long levelId) {
        Object response = adminService.getGrammars(authentication.getName(), levelId);
        return ApiResponse.success("Danh sách từ vựng theo chủ đề", response);
    }

    @GetMapping("/grammar/{id}")
    @Operation(summary = "Chi tiết ngữ pháp (Admin) ")
    public ApiResponse<?> getGrammarById(Authentication authentication, @PathVariable Long id) {
        Object response = adminService.getGrammarByid(authentication.getName(), id);
        return ApiResponse.success("Success", response);
    }

    @PostMapping("/grammar")
    @Operation(summary = "thêm ngữ pháp (Admin)")
    public ApiResponse<?> createGrammarByAdmin(@RequestBody GrammarRequest request) {
        Object response = grammarService.createGrammarByAdmin(request);
        return ApiResponse.success("Thêm mới ngữ pháp thành công", response);
    }

    @PutMapping("/grammar/{id}")
    @Operation(summary = "Cập nhật ngữ pháp (Admin)")
    public ApiResponse<?> updateGrammarByAdmin(@PathVariable Long id, @RequestBody GrammarRequest request) {
        Object response = grammarService.updateGrammarByAdmin(id, request);
        return ApiResponse.success("Cập nhật ngữ pháp thành công", response);
    }

    @DeleteMapping("/grammar/{id}")
    @Operation(summary = "xóa ngữ pháp (Admin)")
    public ApiResponse<?> deleteGrammarByAdmin(@PathVariable Long id) {
        Object response = grammarService.deleteGrammarByAdmin(id);
        return ApiResponse.success("Xóa ngữ pháp thành công", response);
    }

    // level
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
        return ApiResponse.success("Thêm mới level thành công", response);
    }

    @PutMapping("/levels/{id}")
    @Operation(summary = "Cập nhật  level (Admin)")
    public ApiResponse<?> updateLevels(@PathVariable Long id, @RequestBody LevelRequest request) {
        Object response = levelsService.updateLevelByid(id, request);
        return ApiResponse.success("Cập nhật level thành công", response);
    }

    @DeleteMapping("/levels/{id}")
    @Operation(summary = "Xóa  level (Admin)")
    public ApiResponse<?> deleteLevels(@PathVariable Long id) {
        Object response = levelsService.deleteLevelByid(id);
        return ApiResponse.success("Xóa level thành công", response);
    }

    // topic
    @GetMapping("/topics")
    @Operation(summary = "Danh sách  topic theo level (Admin)")
    public ApiResponse<?> getTopics(@RequestParam Long LevelId) {
        Object response = adminService.getTopics(LevelId);
        return ApiResponse.success("Danh sách toppics theo level", response);
    }

    @GetMapping("/topics/{id}")
    @Operation(summary = "Chi tiết topic (Admin)")
    public ApiResponse<?> getTopicsById(@PathVariable Long id) {
        Object response = adminService.getTopicsById(id);
        return ApiResponse.success("Chi tiết toppics ", response);
    }

    @PostMapping("/topics")
    @Operation(summary = "Thêm mới topic (Admin)")
    public ApiResponse<?> creatTopics(@RequestBody TopicRequest request) {
        Object response = topicsService.createTopics(request);
        return ApiResponse.success("Thêm topic thành công", response);
    }

    @PutMapping("/topics/{id}")
    @Operation(summary = "Cập nhật topic (Admin)")
    public ApiResponse<?> updateTopics(@PathVariable Long id, @RequestBody TopicRequest request) {
        Object response = topicsService.updateTopics(id, request);
        return ApiResponse.success("Cập nhật topic thành công", response);
    }

    @DeleteMapping("/topics/{id}")
    @Operation(summary = "Xóa topic (Admin)")
    public ApiResponse<?> deleteTopics(@PathVariable Long id) {
        Object response = topicsService.deleteTopics(id);
        return ApiResponse.success("Xóa topic thành công", response);
    }

    // conversation
    @GetMapping("/conversations")
    @Operation(summary = "Danh sách conversations theo Topic (Admin)")
    public ApiResponse<?> getConversations(@RequestParam Long TopicId) {
        Object respone = adminService.getConversations(TopicId);
        return ApiResponse.success("Danh sách hội thoại theo topic", respone);
    }

    @GetMapping("/conversations/{id}")
    @Operation(summary = "Chi tiết conversations (Admin)")
    public ApiResponse<?> getConversationsById(@PathVariable Long id) {
        Object respone = conversationsService.getConversationById(id);
        return ApiResponse.success("Chi tiết hội thoại", respone);
    }

    @PostMapping("/conversations")
    @Operation(summary = "Thêm mới conversations (Admin)")
    public ApiResponse<?> createConversationsById(@RequestBody ConversationRequest request) {
        Object respone = conversationsService.createConversation(request);
        return ApiResponse.success("Thêm mới thành công", respone);
    }

    @PutMapping("/conversations/{id}")
    @Operation(summary = "Cập nhật conversations (Admin)")
    public ApiResponse<?> updateConversationsById(@PathVariable Long id, @RequestBody ConversationRequest request) {
        Object respone = conversationsService.updateConversationById(id, request);
        return ApiResponse.success("Cập nhật thành công", respone);
    }

    @DeleteMapping("/conversations/{id}")
    @Operation(summary = "Xóa conversations (Admin)")
    public ApiResponse<?> updateConversationsById(@PathVariable Long id) {
        Object respone = conversationsService.deleteConversationById(id);
        return ApiResponse.success("Xóa thành công", respone);
    }

    // exercies
    @GetMapping("/exercies")
    @Operation(summary = "Danh sách exercies theo chủ đề (Admin)")
    public ApiResponse<?> getExercies(@RequestParam Long TopicId) {
        Object respone = adminService.getExercies(TopicId);
        return ApiResponse.success("Danh sách exercies theo chủ đề", respone);
    }

    @GetMapping("/exercies/{id}")
    @Operation(summary = "Danh sách exercies theo chủ đề (Admin)")
    public ApiResponse<?> getExerciesById(@PathVariable Long id) {
        Object respone = exercisesService.getExerciesById(id);
        return ApiResponse.success("Chi tiết exercies", respone);
    }

    @PostMapping(value = "/exercies", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Thêm mới exercies (Admin)")
    public ApiResponse<?> createExercise(
            @RequestParam("topicId") Long topicId,
            @RequestParam("title") String title,
            @RequestParam("type") String type,
            @RequestParam("duration") int duration,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "audioFile", required = false) MultipartFile audioFile) throws Exception {


        ExercisesRequest req = new ExercisesRequest();
        req.setTopicId(topicId);
        req.setTitle(title);
        req.setType(type);
        req.setDuration(duration);
        req.setCategory(category);           
        Object respone = exercisesService.createExercise(req, audioFile);
        return ApiResponse.success("Tạo exercises thành công", respone);
    }

    @PutMapping(value = "/exercies/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Cập nhật  exercies (Admin)")
    public ApiResponse<?> updateExercise(@PathVariable Long id,
            @RequestParam("topicId") Long topicId,
            @RequestParam("title") String title,
            @RequestParam("type") String type,
            @RequestParam("duration") int duration,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "audioFile", required = false) MultipartFile audioFile) throws Exception {

        ExercisesRequest req = new ExercisesRequest();
        req.setTopicId(topicId);
        req.setTitle(title);
        req.setType(type);
        req.setDuration(duration);
        req.setCategory(category);           
        Object respone = exercisesService.updateExercise(id,req, audioFile);
        return ApiResponse.success("Cập nhật exercises thành công", respone);
    }

    @DeleteMapping("/exercies/{id}")
    @Operation(summary = "Xóa exercies (Admin)")
    public ApiResponse<?> deleteExerciesById(@PathVariable Long id) {
        Object respone = exercisesService.deleteExercise(id);
        return ApiResponse.success("Xóa exercies thành công", respone);
    }


}

package com.learnenglish.LearnEnglish.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnenglish.LearnEnglish.dto.requests.UserStatusRequest;
import com.learnenglish.LearnEnglish.dto.responses.ConverSationRespone;
import com.learnenglish.LearnEnglish.dto.responses.ExercisesRespone;
import com.learnenglish.LearnEnglish.dto.responses.GrammarRespone;
import com.learnenglish.LearnEnglish.dto.responses.TopicsRespone;
import com.learnenglish.LearnEnglish.dto.responses.UserResponse;
import com.learnenglish.LearnEnglish.dto.responses.VocaBularyRespone;
import com.learnenglish.LearnEnglish.entity.Exercises;
import com.learnenglish.LearnEnglish.entity.Grammar;
import com.learnenglish.LearnEnglish.entity.Levels;
import com.learnenglish.LearnEnglish.entity.Topics;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.entity.User_vocab_progress;
import com.learnenglish.LearnEnglish.entity.Vocabularies;
import com.learnenglish.LearnEnglish.entity.imp.TopicSummary;
import com.learnenglish.LearnEnglish.exception.AuthorizationException;
import com.learnenglish.LearnEnglish.exception.ValidationException;
import com.learnenglish.LearnEnglish.mapper.ExerciesMapper;
import com.learnenglish.LearnEnglish.mapper.GrammarMapper;
import com.learnenglish.LearnEnglish.mapper.TopicMapper;
import com.learnenglish.LearnEnglish.mapper.UserMapper;
import com.learnenglish.LearnEnglish.mapper.VocabMapper;
import com.learnenglish.LearnEnglish.repository.ExercisesRepository;
import com.learnenglish.LearnEnglish.repository.GrammarRepository;
import com.learnenglish.LearnEnglish.repository.LevelsRepository;
import com.learnenglish.LearnEnglish.repository.TopicsRepository;
import com.learnenglish.LearnEnglish.repository.UserRepository;
import com.learnenglish.LearnEnglish.repository.UserVocabProgressRepository;
import com.learnenglish.LearnEnglish.repository.VocabulariesRepository;

@Service
public class AdminService {
    @Autowired
    VocabulariesRepository vocabulariesRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TopicsRepository topicsRepository;
    @Autowired
    UserVocabProgressRepository userVocabProgressRepository;
    @Autowired
    private VocabMapper vocabMapper;
    @Autowired
    GrammarMapper grammarMapper;
    @Autowired
    GrammarRepository grammarRepository;
    @Autowired
    LevelsRepository levelsRepository;
    @Autowired
    TopicsService topicsService;
    @Autowired
    TopicMapper topicMapper;


    @Autowired
    ExerciesMapper exerciesMapper;
    @Autowired
    ExercisesRepository exercisesRepository;

     @Autowired
    private UserMapper userMapper;
    // get list vocabularies
    public List<VocaBularyRespone> getVocabularies(String email, Long topicId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Không tìm thấy tài khoản với email này"));
        List<Vocabularies> lst = vocabulariesRepository.findByTopicId(topicId);
        return vocabMapper.toListDTO(lst, user);
    }

    // get by id vocabularies
    public VocaBularyRespone getVocabulariesById(String email, Long id) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Không tìm thấy tài khoản với email này"));
        Vocabularies voca = vocabulariesRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Vocabulary không tồn tại"));
        boolean is_learned = userVocabProgressRepository
                .findByUserIdAndVocabularyId(user.getId(), voca.getId())
                .map(User_vocab_progress::isLearned)
                .orElse(false);
        return vocabMapper.toDTO(voca, is_learned);
    }

    // get list grammar
    public List<GrammarRespone> getGrammars(String email, Long LevelId) {
        Levels level = levelsRepository.findById(LevelId)
                .orElseThrow(() -> new ValidationException("Level không hợp lệ"));
        List<Grammar> grammars = grammarRepository.findByLevel(level);
        return grammarMapper.toListDTO(grammars);
    }

    // get grammar by id
    public GrammarRespone getGrammarByid(String email, Long id) {
        Grammar grammar = grammarRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Không tìm thấy grammar"));
        return grammarMapper.toDTO(grammar);
    }

    //get list topic
    public List<TopicsRespone> getTopics (Long levelId)
    {
        Levels level=levelsRepository.findById(levelId)
        .orElseThrow(()-> new ValidationException("Level không tồn tại"));
        List<TopicSummary> lst=topicsRepository.findTopicSummariesByLevel(level);
        return topicMapper.toListDTO(lst);
    }
    //get topic by id
    public TopicsRespone getTopicsById (Long levelId)
    {
        TopicSummary topic=topicsRepository.findTopicSummaryById(levelId);
               if (topic == null) throw new ValidationException("Topic không tồn tại");
        return topicMapper.toDTO(topic);
    }

    //getlist conversation
   

    //Exercies
    public List<ExercisesRespone> getExercies(Long topicId)
    {
       
        Topics topic = topicsRepository.findById(topicId)
                .orElseThrow(() -> new ValidationException("Không tìm thấy Topics "));
        List<Exercises> respones=exercisesRepository.findByTopic(topic);
        return exerciesMapper.toListDTO(respones);  
    }

    
    // Lấy TOÀN BỘ từ vựng trong hệ thống (Không phân biệt topic)
    public List<VocaBularyRespone> getAllVocabularies() {
        List<Vocabularies> lst = vocabulariesRepository.findAll();
        return vocabMapper.toListDTO(lst, null); 
    }

    // Lấy TOÀN BỘ ngữ pháp trong hệ thống
    public List<GrammarRespone> getAllGrammars() {
        List<Grammar> grammars = grammarRepository.findAll();
        return grammarMapper.toListDTO(grammars);
    }

    // Lấy TOÀN BỘ chủ đề trong hệ thống
    public List<TopicsRespone> getAllTopics() {
        List<TopicSummary> lst = topicsRepository.findAllTopicSummaries(); 
        return topicMapper.toListDTO(lst);
    }

    // Lấy TOÀN BỘ bài tập trong hệ thống
    public List<ExercisesRespone> getAllExercises() {
        List<Exercises> responses = exercisesRepository.findAll();
        return exerciesMapper.toListDTO(responses);  
    }

  
   

    // --- QUẢN LÝ USER CHO ADMIN ---

    // 1. Lấy danh sách tất cả Users
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toListDTO(users);
    }

    // 2. Lấy chi tiết 1 User
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Không tìm thấy người dùng này"));
        return userMapper.toDTO(user);
    }
    public UserResponse updateUserStatus(Long id, UserStatusRequest req) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Không tìm thấy người dùng này"));

        if (req.getStatus() != null) {
            try {
                user.setStatus(User.Status.valueOf(req.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new ValidationException("Trạng thái không hợp lệ. Chỉ chấp nhận ACTIVE hoặc LOCKED");
            }
        }

        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }
}

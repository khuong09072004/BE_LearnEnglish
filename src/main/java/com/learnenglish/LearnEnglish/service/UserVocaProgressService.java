package com.learnenglish.LearnEnglish.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnenglish.LearnEnglish.dto.responses.VocaBularyRespone;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.entity.User_vocab_progress;
import com.learnenglish.LearnEnglish.entity.Vocabularies;
import com.learnenglish.LearnEnglish.exception.ValidationException;
import com.learnenglish.LearnEnglish.mapper.VocabMapper;
import com.learnenglish.LearnEnglish.repository.UserRepository;
import com.learnenglish.LearnEnglish.repository.UserVocabProgressRepository;
import com.learnenglish.LearnEnglish.repository.VocabulariesRepository;

import jakarta.transaction.Transactional;

@Service
public class UserVocaProgressService {
    @Autowired 
    UserVocabProgressRepository userVocabProgressRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    VocabulariesRepository vocabulariesRepository;
    @Autowired
    private VocabMapper vocabMapper;
    @Transactional
    public VocaBularyRespone learnVocaByUser(String email,Long vocabId)
    {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ValidationException("Không tìm thấy tài khoản với email này"));
        Vocabularies vocab=vocabulariesRepository.findById(vocabId)
        .orElseThrow(() -> new ValidationException("Vocabulary không tồn tại")); 
        User_vocab_progress userVocabProgress= userVocabProgressRepository
            .findByUserIdAndVocabularyId(user.getId(), vocab.getId())
            .orElse(new User_vocab_progress());
        userVocabProgress.setUser(user);
        userVocabProgress.setVocabulary(vocab);
        userVocabProgress.setLearned(true);  
        userVocabProgress.setLearnedAt(LocalDateTime.now());
        userVocabProgressRepository.save(userVocabProgress);
        return vocabMapper.toDTO(vocab, userVocabProgress.isLearned());
    }

    @Transactional
    public VocaBularyRespone unlearnVocaByUser(String email,Long vocabId)
    {
         User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ValidationException("Không tìm thấy tài khoản với email này"));
        Vocabularies vocab=vocabulariesRepository.findById(vocabId)
        .orElseThrow(() -> new ValidationException("Vocabulary không tồn tại")); 
        User_vocab_progress userVocabProgress= userVocabProgressRepository
            .findByUserIdAndVocabularyId(user.getId(), vocab.getId())
            .orElseThrow(()->new ValidationException("progress không tồn tại"));
        userVocabProgress.setLearned(false);  
        userVocabProgress.setLearnedAt(LocalDateTime.now());
        userVocabProgressRepository.save(userVocabProgress);
        return vocabMapper.toDTO(vocab, userVocabProgress.isLearned());
    }

    public Object getVocabStatisticsInTopic (String email, Long topicId)
    {
         User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ValidationException("User không tồn tại"));
        int total = vocabulariesRepository.countByTopicId(topicId);
        int learned = userVocabProgressRepository.countLearnedByUserAndTopic(user.getId(), topicId);
        int unlearned = total - learned;
        Map<String, Integer> result = new HashMap<>();
        result.put("total", total);
        result.put("learned", learned);
        result.put("unlearned", unlearned);
        return result;
    }

}

package com.learnenglish.LearnEnglish.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnenglish.LearnEnglish.dto.responses.VocaBularyRespone;
import com.learnenglish.LearnEnglish.entity.Topics;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.entity.User_vocab_progress;
import com.learnenglish.LearnEnglish.entity.Vocabularies;
import com.learnenglish.LearnEnglish.exception.AuthorizationException;
import com.learnenglish.LearnEnglish.exception.ValidationException;
import com.learnenglish.LearnEnglish.mapper.VocabMapper;
import com.learnenglish.LearnEnglish.repository.TopicsRepository;
import com.learnenglish.LearnEnglish.repository.UserRepository;
import com.learnenglish.LearnEnglish.repository.UserVocabProgressRepository;
import com.learnenglish.LearnEnglish.repository.VocabulariesRepository;

@Service
public class VocabulariesService {
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

    private List<VocaBularyRespone> maperRespones(List<Vocabularies> lst,User user)
    {
        List<VocaBularyRespone> respones=new ArrayList<>();
        for(Vocabularies item : lst)
        {
            boolean is_learned = userVocabProgressRepository
                .findByUserIdAndVocabularyId(user.getId(), item.getId())
                .map(User_vocab_progress::isLearned)
                .orElse(false);
            VocaBularyRespone dto=vocabMapper.toDTO(item,is_learned);
            respones.add(dto);
        }
        return respones;
    }

    public List<VocaBularyRespone> getVocabularies(String email,Long topicId)
    {
        
         User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ValidationException("Không tìm thấy tài khoản với email này"));

        Topics topic = topicsRepository.findById(topicId)
            .orElseThrow(() -> new ValidationException("Topic không tồn tại"));

        if (!topic.getLevel().getCode().equals(user.getLevel().getCode())) {
            throw new AuthorizationException("Topic này không phù hợp với trình độ của bạn");
        }

        List<Vocabularies> lst=vocabulariesRepository.findByTopicId(topicId);
        return maperRespones(lst,user);
    }

    public VocaBularyRespone getVocabulariesById(String email,Long id)
    {
         User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ValidationException("Không tìm thấy tài khoản với email này"));
        Vocabularies voca=vocabulariesRepository.findById(id)
        .orElseThrow(() -> new ValidationException("Vocabulary không tồn tại"));
        boolean is_learned = userVocabProgressRepository
                .findByUserIdAndVocabularyId(user.getId(), voca.getId())
                .map(User_vocab_progress::isLearned)
                .orElse(false);
        return vocabMapper.toDTO(voca,is_learned);
    }
}

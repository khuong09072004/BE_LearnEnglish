package com.learnenglish.LearnEnglish.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnenglish.LearnEnglish.dto.responses.VocaBularyRespone;
import com.learnenglish.LearnEnglish.entity.Topics;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.entity.Vocabularies;
import com.learnenglish.LearnEnglish.exception.AuthorizationException;
import com.learnenglish.LearnEnglish.exception.ValidationException;
import com.learnenglish.LearnEnglish.repository.TopicsRepository;
import com.learnenglish.LearnEnglish.repository.UserRepository;
import com.learnenglish.LearnEnglish.repository.VocabulariesRepository;

@Service
public class VocabulariesService {
    @Autowired
    VocabulariesRepository vocabulariesRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TopicsRepository topicsRepository;
    private VocaBularyRespone maptoDTO(Vocabularies item)
    {
        return new VocaBularyRespone(item.getId(),item.getTopic().getId(),item.getWord(),item.getMeaning(),item.getPhonetic(),item.getDescription(),item.getImage_url());
    }

    private List<VocaBularyRespone> maperRespones(List<Vocabularies> lst)
    {
        List<VocaBularyRespone> respones=new ArrayList<>();
        for(Vocabularies item : lst)
        {
            VocaBularyRespone dto=maptoDTO(item);
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

        if (!topic.getLevel().equals(user.getUserLevel().name())) {
            throw new AuthorizationException("Topic này không phù hợp với trình độ của bạn");
        }

        List<Vocabularies> lst=vocabulariesRepository.findByTopicId(topicId);
        return maperRespones(lst);
    }
}

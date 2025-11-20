package com.learnenglish.LearnEnglish.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnenglish.LearnEnglish.dto.responses.TopicsRespone;
import com.learnenglish.LearnEnglish.entity.Topics;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.exception.ValidationException;
import com.learnenglish.LearnEnglish.repository.TopicsRepository;
import com.learnenglish.LearnEnglish.repository.UserRepository;

@Service
public class TopicsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TopicsRepository topicsRepository;

    private List<TopicsRespone> mapperTopicRespone(List<Topics> lst) {
        List<TopicsRespone> responses = new ArrayList<>();
        for (Topics item : lst) {
            TopicsRespone dto = new TopicsRespone(item.getId(), item.getName(), item.getLevel().getCode());
            responses.add(dto);
        }
        return responses;
    }

    public List<TopicsRespone> getTopics (String email)
    {
         User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Không tìm thấy tài khoản với email này"));
        List<Topics> lst=topicsRepository.findByLevel(user.getLevel());
        return mapperTopicRespone(lst);
    }
}

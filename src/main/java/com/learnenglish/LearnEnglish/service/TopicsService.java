package com.learnenglish.LearnEnglish.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnenglish.LearnEnglish.dto.requests.TopicRequest;
import com.learnenglish.LearnEnglish.dto.responses.TopicsRespone;
import com.learnenglish.LearnEnglish.entity.Levels;
import com.learnenglish.LearnEnglish.entity.Topics;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.entity.imp.TopicSummary;
import com.learnenglish.LearnEnglish.exception.ValidationException;
import com.learnenglish.LearnEnglish.mapper.TopicMapper;
import com.learnenglish.LearnEnglish.repository.LevelsRepository;
import com.learnenglish.LearnEnglish.repository.TopicsRepository;
import com.learnenglish.LearnEnglish.repository.UserRepository;

@Service
public class TopicsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TopicsRepository topicsRepository;
    @Autowired
    TopicMapper topicMapper;
    @Autowired
    LevelsRepository levelsRepository;
    //get topic by User
    public List<TopicsRespone> getTopics (String email)
    {
         User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Không tìm thấy tài khoản với email này"));
        List<TopicSummary> lst=topicsRepository.findTopicSummariesByLevel(user.getLevel());
        return topicMapper.toListDTO(lst);
    }
    //create 
    public TopicsRespone createTopics(TopicRequest request)
    {
        Levels level=levelsRepository.findById(request.getLevelId())
        .orElseThrow(()-> new ValidationException("Level không tồn tại"));
        Topics topic =new Topics();
        topic.setName(request.getName());
        topic.setLevel(level);
        topic.setCreatedAt(LocalDateTime.now());
        topicsRepository.save(topic);
        TopicSummary summary = topicsRepository.findTopicSummaryById(topic.getId());
        return topicMapper.toDTO(summary);
    }
    //update
    public TopicsRespone updateTopics(Long id,TopicRequest request)
    {
        Levels level=levelsRepository.findById(request.getLevelId())
        .orElseThrow(()-> new ValidationException("Level không tồn tại"));
        Topics topic=topicsRepository.findById(id)
                .orElseThrow(()->new ValidationException("Topic không tồn tại"));;
        topic.setName(request.getName());
        topic.setLevel(level);
        topic.setCreatedAt(LocalDateTime.now());
        topicsRepository.save(topic);
        TopicSummary summary = topicsRepository.findTopicSummaryById(topic.getId());
        return topicMapper.toDTO(summary);
    }
    //delete
    public TopicsRespone deleteTopics(Long id)
    {
        Topics topic=topicsRepository.findById(id)
                .orElseThrow(()->new ValidationException("Topic không tồn tại"));;
        topicsRepository.delete(topic);
        TopicSummary summary = topicsRepository.findTopicSummaryById(topic.getId());
        return topicMapper.toDTO(summary);
    }
}

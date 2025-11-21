package com.learnenglish.LearnEnglish.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Retry.Topic;
import org.springframework.stereotype.Service;

import com.learnenglish.LearnEnglish.dto.responses.ConverSationRespone;
import com.learnenglish.LearnEnglish.entity.Conversations;
import com.learnenglish.LearnEnglish.entity.Topics;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.exception.ValidationException;
import com.learnenglish.LearnEnglish.repository.ConversationsRepository;
import com.learnenglish.LearnEnglish.repository.TopicsRepository;
import com.learnenglish.LearnEnglish.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ConversationsService {
    @Autowired
    ConversationsRepository conversationsRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired 
    TopicsRepository topicsRepository;

    private ConverSationRespone mapToDto(Conversations item)
    {
        return new ConverSationRespone(item.getId(), item.getTopic().getId(), item.getTitle(), item.getContext(), item.getRoles(), item.getScript());
    }

    private List<ConverSationRespone> mapToRespone(List<Conversations> lst)
    {
        List<ConverSationRespone> respones=new ArrayList<>();
        for(Conversations item : lst)
        {
            ConverSationRespone dto=mapToDto(item);
            respones.add(dto);
        }
        return respones;
    }

    public List<ConverSationRespone> getConversations(String email,Long topicId)
    {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Tài khoản không tồn tại trong hệ thống"));
       
        Topics topic = topicsRepository.findById(topicId)
                .orElseThrow(() -> new ValidationException("Không tìm thấy Topics "));
        if(!user.getLevel().getCode().equals(topic.getLevel().getCode()))
        {
            throw new ValidationException("Topic không phù hợp với trình độ của người học");
        }
        List<Conversations> conversations=conversationsRepository.findByTopic(topic.getId());
        return mapToRespone(conversations);
    
    }
}

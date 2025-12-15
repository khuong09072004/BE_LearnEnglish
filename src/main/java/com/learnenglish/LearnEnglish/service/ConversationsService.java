package com.learnenglish.LearnEnglish.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Retry.Topic;
import org.springframework.stereotype.Service;

import com.learnenglish.LearnEnglish.dto.requests.ConversationRequest;
import com.learnenglish.LearnEnglish.dto.responses.ConverSationRespone;
import com.learnenglish.LearnEnglish.entity.Conversations;
import com.learnenglish.LearnEnglish.entity.Levels;
import com.learnenglish.LearnEnglish.entity.Topics;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.exception.ValidationException;
import com.learnenglish.LearnEnglish.mapper.ConverSationMapper;
import com.learnenglish.LearnEnglish.repository.ConversationsRepository;
import com.learnenglish.LearnEnglish.repository.TopicsRepository;
import com.learnenglish.LearnEnglish.repository.UserRepository;
import com.learnenglish.LearnEnglish.util.UserLevelHelper;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ConversationsService {
    @Autowired
    ConversationsRepository conversationsRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired 
    TopicsRepository topicsRepository;
    @Autowired
    ConverSationMapper converSationMapper;
    @Autowired
    private UserLevelHelper userLevelHelper;
    public List<ConverSationRespone> getConversations(String email,Long topicId)
    {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Tài khoản không tồn tại trong hệ thống"));
       
        Topics topic = topicsRepository.findById(topicId)
                .orElseThrow(() -> new ValidationException("Không tìm thấy Topics "));
        Levels studyingLevel = userLevelHelper.getStudyingLevel(user);
        if(!studyingLevel.getCode().equals(topic.getLevel().getCode()))
        {
            throw new ValidationException("Topic không phù hợp với trình độ của người học");
        }
        List<Conversations> conversations=conversationsRepository.findByTopic(topic.getId());
        return converSationMapper.toListDTO(conversations);
    
    }

    public ConverSationRespone getConversationById(Long id)
    {
         Conversations conversation=conversationsRepository.findById(id)
         .orElseThrow(()->new ValidationException("Không tìm thấy conversation"));
         return converSationMapper.toDTO(conversation);
    }

    public  ConverSationRespone createConversation(ConversationRequest request)
    {
            Topics topic = topicsRepository.findById(request.getTopicId())
                .orElseThrow(() -> new ValidationException("Không tìm thấy Topics "));
            Conversations conversation=new Conversations();
            conversation.setContext(request.getContext());
            conversation.setTitle(request.getTitle());
            conversation.setTopic(topic);
            conversation.setRoles(request.getRoles());
            conversation.setScript(request.getScript());
            conversationsRepository.save(conversation);
            return converSationMapper.toDTO(conversation);
    }

    public  ConverSationRespone updateConversationById(Long id,ConversationRequest request)
    {
            Topics topic = topicsRepository.findById(request.getTopicId())
                .orElseThrow(() -> new ValidationException("Không tìm thấy Topics "));
            Conversations conversation=conversationsRepository.findById(id)
            .orElseThrow(()->new ValidationException("Không tìm thấy conversation"));
            conversation.setContext(request.getContext());
            conversation.setTitle(request.getTitle());
            conversation.setTopic(topic);
            conversation.setRoles(request.getRoles());
            conversation.setScript(request.getScript());
            conversationsRepository.save(conversation);
            return converSationMapper.toDTO(conversation);
    }

    public  ConverSationRespone deleteConversationById(Long id)
    {
            Conversations conversation=conversationsRepository.findById(id)
            .orElseThrow(()->new ValidationException("Không tìm thấy conversation"));
            conversationsRepository.delete(conversation);
            return converSationMapper.toDTO(conversation);
    }
}

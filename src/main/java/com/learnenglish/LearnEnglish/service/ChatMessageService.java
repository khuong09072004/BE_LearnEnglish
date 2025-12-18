package com.learnenglish.LearnEnglish.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learnenglish.LearnEnglish.dto.responses.ChatMessageResponse;
import com.learnenglish.LearnEnglish.entity.Chat_messages;
import com.learnenglish.LearnEnglish.entity.Topics;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.repository.ChatMessageRepository;
import com.learnenglish.LearnEnglish.repository.TopicsRepository;
import com.learnenglish.LearnEnglish.repository.UserRepository;

@Service
public class ChatMessageService {
    @Autowired
    private  ChatMessageRepository chatMessageRepository;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private TopicsRepository topicRepository;

    @Transactional
    public Chat_messages saveMessage(Long userId, Long topicId, String content) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Topics topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found"));

        Chat_messages entity = new Chat_messages();
        entity.setUser(user);
        entity.setTopic(topic);
        entity.setMessage(content);
        entity.setSentAt(LocalDateTime.now());

        return chatMessageRepository.save(entity);
    }

    //Get List Message
     public List<ChatMessageResponse> getMessagesByTopic(
            Long topicId,
            int page,
            int size
    ) {
        return chatMessageRepository
                .findByTopic(
                        topicId,
                        PageRequest.of(page, size)
                )
                .map(msg -> new ChatMessageResponse(
                        msg.getId(),
                        msg.getTopic().getId(),
                        msg.getUser().getId(),
                        msg.getUser().getFullName(),
                        msg.getUser().getAvatar(),
                        msg.getMessage(),
                        msg.getSentAt()
                ))
                .toList();
    }
}

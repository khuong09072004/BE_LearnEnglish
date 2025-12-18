package com.learnenglish.LearnEnglish.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import com.learnenglish.LearnEnglish.dto.requests.ChatMessageRequest;
import com.learnenglish.LearnEnglish.dto.responses.ChatMessageResponse;
import com.learnenglish.LearnEnglish.entity.Chat_messages;
import com.learnenglish.LearnEnglish.service.ChatMessageService;

@Controller
public class WebSocketController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private ChatMessageService chatMessageService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageRequest request,
            SimpMessageHeaderAccessor headerAccessor) {

        var sessionAttrs = headerAccessor.getSessionAttributes();
        if (sessionAttrs == null)
            return;

        Long userId = (Long) sessionAttrs.get("userId");
        if (userId == null)
            return;

        Chat_messages saved = chatMessageService.saveMessage(
                userId,
                request.getTopicId(),
                request.getContent());

        ChatMessageResponse response = new ChatMessageResponse(
                saved.getId(),
                request.getTopicId(),
                userId,
                saved.getUser().getFullName(),
                saved.getUser().getAvatar(),
                saved.getMessage(),
                saved.getSentAt());
      


        messagingTemplate.convertAndSend(
                "/topic/chat/" + request.getTopicId(),
                response);
    }

}

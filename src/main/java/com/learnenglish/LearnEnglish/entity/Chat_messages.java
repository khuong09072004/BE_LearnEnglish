package com.learnenglish.LearnEnglish.entity;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chat_messages")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Chat_messages {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topics topic;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User user;

    @Column(name = "message")
    private String message;
    
    @Column(name = "sent_at")
    private LocalDateTime sentAt;
}


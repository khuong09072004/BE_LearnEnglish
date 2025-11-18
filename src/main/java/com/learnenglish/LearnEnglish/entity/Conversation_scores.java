package com.learnenglish.LearnEnglish.entity;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "conversation_scores")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Conversation_scores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversations conversation;

    @Column(name="writing_score")
    private int writing_score;

    @Column(name="speaking_score")
    private int speaking_score;
    
    @Column(name="recorded_audio_url")
    private String recorded_audio_url;
    
    @Column(name = "feedback")
    private String feedback;
    
    @Column(name = "completed_at")
    private LocalDateTime completed_at;
}

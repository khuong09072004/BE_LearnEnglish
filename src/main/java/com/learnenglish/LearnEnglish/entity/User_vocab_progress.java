package com.learnenglish.LearnEnglish.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_vocab_progress")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User_vocab_progress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "vocab_id")
    private Vocabularies vocabulary;

    @Column(name = "is_learned")
    private boolean is_learned;

    @Column(name = "learned_at")
    private LocalDateTime learned_at = LocalDateTime.now();

}

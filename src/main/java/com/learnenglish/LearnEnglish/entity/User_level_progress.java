package com.learnenglish.LearnEnglish.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_level_progress")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User_level_progress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "level")
    @Enumerated(EnumType.STRING)
    private UserLevel level;
    @Column(name = "progress")
    private int progress;
    @Column(name = "is_completed")
    private boolean is_completed;
    @Column(name = "completed_at")
    private LocalDateTime completed_at;

    public enum UserLevel {
        BEGINNER, A1, A2, B1, B2, C1, C2
    }
}

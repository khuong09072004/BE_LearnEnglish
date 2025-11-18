package com.learnenglish.LearnEnglish.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exercise_results")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Exercise_results {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercises exercise;
    
    @Column(name = "answers", columnDefinition = "json")
    private String answers;
    
    @Column(name = "score")
    private int score;
    
    @Column(name = "completed_at")
    private LocalDateTime completed_at;
}

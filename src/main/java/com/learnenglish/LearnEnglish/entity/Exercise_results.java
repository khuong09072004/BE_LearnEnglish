package com.learnenglish.LearnEnglish.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.JsonNode;
import com.learnenglish.LearnEnglish.convert.JsonNodeConverter;

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
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode answers;

    @Column(name = "score")
    private int score;
    @Column(name = "is_correct_count")
    private int isCorrectCount;
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}

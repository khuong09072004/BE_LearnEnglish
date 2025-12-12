package com.learnenglish.LearnEnglish.entity;

import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.learnenglish.LearnEnglish.convert.JsonNodeConverter;
import com.learnenglish.LearnEnglish.dto.enums.ExerciseType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "exercises")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exercises {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topics topic;

    private String title;

    @Column(name = "audio_url")
    private String audioUrl;

    @Enumerated(EnumType.STRING)
    private ExerciseType type;

    @Enumerated(EnumType.STRING)
    private ExerciseCategory category;
    @Column(name = "duration")
    private int duration;
    @Column(name = "passing_id")
    private Integer passingId;
    // Quan hệ 1 → N sang exercise_items
    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ExerciseItems> exerciseItems;

    public enum ExerciseCategory {
        VOCAB, GRAMMAR, LISTENING, READING, WRITING
    }
    @OneToMany(mappedBy = "exercise")
    private Set<Exercise_results> ListExerciseResults;
}

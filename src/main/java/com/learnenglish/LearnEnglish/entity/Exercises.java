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

    @Column(name = "title")
    private String title;
    @Column(name = "audio_url")
    private String audioUrl;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ExerciseType type;

    @Column(name = "questions", columnDefinition = "json")
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode questions;
    @Column(name = "answer_key", columnDefinition = "json")
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode answerKey;
    @Column(name = "duration")
    private int duration;
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private ExerciseCategory category;

    public enum ExerciseCategory {
        VOCAB, GRAMMAR, LISTENING, READING, WRITING
    }
    @OneToMany(mappedBy = "exercise")
    private Set<Exercise_results> ListExerciseResults;
}

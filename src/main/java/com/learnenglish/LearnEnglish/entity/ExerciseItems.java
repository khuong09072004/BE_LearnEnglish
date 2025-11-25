package com.learnenglish.LearnEnglish.entity;



import com.fasterxml.jackson.databind.JsonNode;
import com.learnenglish.LearnEnglish.convert.JsonNodeConverter;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "exercise_items")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExerciseItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercises exercise;

    private int position;

    @Column(name = "question_json", columnDefinition = "json")
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode questionJson;

    @Column(name = "answer_json", columnDefinition = "json")
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode answerJson;

}


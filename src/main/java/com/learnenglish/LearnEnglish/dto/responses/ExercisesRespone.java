package com.learnenglish.LearnEnglish.dto.responses;

import com.fasterxml.jackson.databind.JsonNode;
import com.learnenglish.LearnEnglish.entity.Topics;

import lombok.*;

@Data
@NoArgsConstructor
public class ExercisesRespone {
    private Long id;
    private Long topicId;
    private String title;
    private String type;
    private String audioURL;
    private int duration;
    private String category;
    private Integer score;
    private Boolean isDone;

   
    public ExercisesRespone(
        Long id,
        Long topicId,
        String title,
        String type,
        String audioURL,
        int duration,
        String category
    ) {
        this.id = id;
        this.topicId = topicId;
        this.title = title;
        this.type = type;
        this.audioURL = audioURL;
        this.duration = duration;
        this.category = category;
    }

   
    public ExercisesRespone(
        Long id,
        Long topicId,
        String title,
        String type,
        String audioURL,
        int duration,
        String category,
        Integer score,
        Boolean isDone
    ) {
        this.id = id;
        this.topicId = topicId;
        this.title = title;
        this.type = type;
        this.audioURL = audioURL;
        this.duration = duration;
        this.category = category;
        this.score = score;
        this.isDone = isDone;
    }
}


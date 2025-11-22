package com.learnenglish.LearnEnglish.dto.responses;

import com.learnenglish.LearnEnglish.entity.Topics;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExercisesRespone {
    private Long id;
    private Long topicId;
    private String title;
    private String type;
    private String questions;
    private String audioURL;
    private int duration;
}

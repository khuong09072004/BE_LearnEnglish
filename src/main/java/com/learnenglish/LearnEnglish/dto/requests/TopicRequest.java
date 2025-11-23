package com.learnenglish.LearnEnglish.dto.requests;
import java.time.LocalDateTime;


import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicRequest {
    private String name;
    private Long levelId;
}

package com.learnenglish.LearnEnglish.dto.responses;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicsRespone {
    private Long id;
    private String name;
    private String level;
}

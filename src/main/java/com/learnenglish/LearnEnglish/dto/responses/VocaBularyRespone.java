package com.learnenglish.LearnEnglish.dto.responses;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VocaBularyRespone {
    private Long id;
    private Long topicId;
    private String word;
    private String meaning;
    private String phonetic;
    private String description;
    private String image_url;
    private boolean islearned;
}

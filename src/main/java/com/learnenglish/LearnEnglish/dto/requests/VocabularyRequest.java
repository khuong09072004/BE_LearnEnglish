package com.learnenglish.LearnEnglish.dto.requests;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VocabularyRequest {
    private Long topicId;
    private String word;
    private String meaning;
    private String phonetic;
    private String description;

    
}

package com.learnenglish.LearnEnglish.dto.requests;

import org.springframework.web.multipart.MultipartFile;

import com.learnenglish.LearnEnglish.entity.Topics;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
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

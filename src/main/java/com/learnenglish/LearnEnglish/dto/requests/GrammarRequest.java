package com.learnenglish.LearnEnglish.dto.requests;
import com.learnenglish.LearnEnglish.entity.Levels;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GrammarRequest {
    private String title;
    private String content;
    private String example;
    private String source;
    private Long levelId;
}

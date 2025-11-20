package com.learnenglish.LearnEnglish.dto.responses;
import com.learnenglish.LearnEnglish.entity.Levels;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrammarRespone {
    private Long id;
    private String title;
    private String content;
    private String example;
    private String source;
    private String level;
}

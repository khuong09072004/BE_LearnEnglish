package com.learnenglish.LearnEnglish.dto.requests;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LevelRequest {
    private String code;
    private String name;
}

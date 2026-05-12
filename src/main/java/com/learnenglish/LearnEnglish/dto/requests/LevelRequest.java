package com.learnenglish.LearnEnglish.dto.requests;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LevelRequest {
    private String code;
    private String name;
    @JsonAlias("level_order")
    private Integer levelOrder;
}

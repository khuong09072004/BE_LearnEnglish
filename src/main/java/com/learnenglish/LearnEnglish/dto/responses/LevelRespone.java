package com.learnenglish.LearnEnglish.dto.responses;

import java.time.LocalDateTime;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LevelRespone {
    private Long id;
    private String code;
    private String name;
    private LocalDateTime created_at = LocalDateTime.now();
}

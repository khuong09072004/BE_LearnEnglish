package com.learnenglish.LearnEnglish.dto.responses;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private String email;
    private String name;
    private String avatar;
}

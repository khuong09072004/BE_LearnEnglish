package com.learnenglish.LearnEnglish.dto.responses;

import java.time.LocalDateTime;

import com.learnenglish.LearnEnglish.dto.enums.NotificationType;
import lombok.*;
@Data
@Builder
public class NotificationSocketResponse {
    private Long id;
    private NotificationType type;
    private String title;
    private String content;
    private String data;
    private LocalDateTime createdAt;
}

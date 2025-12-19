package com.learnenglish.LearnEnglish.dto.responses;

import java.time.LocalDateTime;


import com.learnenglish.LearnEnglish.dto.enums.NotificationType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationResponse {
    private Long id;
    private NotificationType type;
    private String title;
    private String content;
    private String data;
    private boolean read;
    private LocalDateTime createdAt;
}

package com.learnenglish.LearnEnglish.entity;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "activity_logs")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Activity_logs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "action")
    private String action;
    
    @Column(name = "detail", columnDefinition = "json")
    private String detail;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

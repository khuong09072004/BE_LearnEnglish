package com.learnenglish.LearnEnglish.entity;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reports")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Reports {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Column(name = "created_at")
    private LocalDateTime created_at;

    public enum Status {
        PENDING,
        RESOLVED
    }
}

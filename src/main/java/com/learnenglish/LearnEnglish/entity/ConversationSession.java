package com.learnenglish.LearnEnglish.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "conversation_sessions")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationSession {

      @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private ConversationLesson lesson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "current_step")
    private Integer currentStep;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @Column(name = "is_learn")
    private Boolean learned;

    @Column(name = "total_score")
    private Integer totalScore;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;
}

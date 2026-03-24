package com.learnenglish.LearnEnglish.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "conversation_step_attempts",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"session_id", "step_id"})
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationStepAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private ConversationSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "step_id", nullable = false)
    private ConversationStep step;

    @Column(name = "attempt_count")
    private int attemptCount;

    @Column(name = "is_passed")
    private boolean passed;

    @Column(name = "assist_mode")
    private boolean assistMode;

    @Column(name = "last_attempt_at")
    private LocalDateTime lastAttemptAt;
}

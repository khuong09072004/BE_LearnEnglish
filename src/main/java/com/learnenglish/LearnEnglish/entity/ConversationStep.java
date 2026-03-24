package com.learnenglish.LearnEnglish.entity;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "conversation_steps",
       uniqueConstraints = @UniqueConstraint(columnNames = {"lesson_id","step_order"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationStep {

      @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private ConversationLesson lesson;

    @Column(name = "step_order")
    private Integer stepOrder;

    @Column(name = "ai_role")
    private String aiRole;

    @Column(name = "user_task", columnDefinition = "TEXT")
    private String userTask;

    @Column(name = "grammar_focus")
    private String grammarFocus;

    @Column(name = "sample_answer", columnDefinition = "TEXT")
    private String sampleAnswer;

    @Column(name = "max_attempts")
    private Integer maxAttempts;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

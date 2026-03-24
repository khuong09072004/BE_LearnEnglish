package com.learnenglish.LearnEnglish.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "conversation_lessons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationLesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "level_id", nullable = false)
    private Levels level;

    @Enumerated(EnumType.STRING)
    @Column(name = "skill_focus") // BỔ SUNG THÊM name
    private SkillFocus skillFocus;

    @Column(name = "goal", columnDefinition = "TEXT", nullable = false) // BỔ SUNG name cho chắc chắn
    private String goal;

    @Column(name = "system_prompt", columnDefinition = "TEXT", nullable = false) // BỔ SUNG THÊM name
    private String systemPrompt;

    @Column(name = "created_at") // BỔ SUNG THÊM DÒNG NÀY
    private LocalDateTime createdAt;

    public enum SkillFocus {
        SPEAKING, GRAMMAR, VOCAB
    }
}
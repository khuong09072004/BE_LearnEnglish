package com.learnenglish.LearnEnglish.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.learnenglish.LearnEnglish.convert.JsonNodeConverter;

@Entity
@Table(name = "conversation_turns")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationTurn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private ConversationSession session;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Convert(converter = JsonNodeConverter.class)
    @Column(columnDefinition = "TEXT")
    @JsonIgnore
    private JsonNode analysis;

    @Convert(converter = JsonNodeConverter.class)
    @Column(columnDefinition = "TEXT")
    @JsonIgnore
    private JsonNode correction;

    private Integer score;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public enum Role {
        USER, AI
    }
}

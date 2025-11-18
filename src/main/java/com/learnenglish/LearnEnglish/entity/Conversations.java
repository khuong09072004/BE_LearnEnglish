package com.learnenglish.LearnEnglish.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "conversations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Conversations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Đây là relation ManyToOne bắt buộc phải có
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topics topic;

    @Column(name = "title")
    private String title;

    @Column(name = "context")
    private String context;

    @Column(name = "roles")
    private String roles;

    @Column(name = "script")
    private String script;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    private Set<Conversation_scores> conversationScores;
}

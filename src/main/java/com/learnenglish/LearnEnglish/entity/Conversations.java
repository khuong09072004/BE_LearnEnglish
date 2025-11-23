package com.learnenglish.LearnEnglish.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.learnenglish.LearnEnglish.convert.JsonNodeConverter;

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

    @Column(name = "roles",columnDefinition = "json")
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode  roles;

    @Column(name = "script",columnDefinition = "json")
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode script;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    private Set<Conversation_scores> conversationScores;
}

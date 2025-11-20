package com.learnenglish.LearnEnglish.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "topics")
public class Topics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "level_id")
    private Levels level;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy="topic", cascade = CascadeType.ALL)
    private Set<Vocabularies> vocabularies;

    @OneToMany(mappedBy="topic", cascade = CascadeType.ALL)
    private Set<Exercises> exercises;

    @OneToMany(mappedBy="topic", cascade = CascadeType.ALL)
    private Set<Conversations> conversations;
}

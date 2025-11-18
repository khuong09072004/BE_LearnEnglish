package com.learnenglish.LearnEnglish.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "vocabularies")
@NoArgsConstructor
@AllArgsConstructor
@Data

public class Vocabularies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topics topic;
    @Column(name = "word")
    private String word;
    @Column(name = "meaning")
    private String meaning;
    @Column(name = "phonetic")
    private String phonetic;
    @Column(name = "description")
    private String description;
    @Column(name = "image_url")
    private String image_url;

    @OneToMany(mappedBy = "vocabulary")
    private Set<User_vocab_progress> ListVocaUserProgress;
}

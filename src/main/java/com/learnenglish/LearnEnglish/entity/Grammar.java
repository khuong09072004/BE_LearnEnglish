package com.learnenglish.LearnEnglish.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "grammar")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Grammar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "example")
    private String example;

    @Column(name = "source")
    private String source;
}

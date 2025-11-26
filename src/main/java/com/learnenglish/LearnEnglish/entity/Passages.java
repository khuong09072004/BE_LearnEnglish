package com.learnenglish.LearnEnglish.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity(name = "passages")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Passages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name="content")
    private String content;
    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Category category;
    public  enum Category{READING,LISTENING}

}

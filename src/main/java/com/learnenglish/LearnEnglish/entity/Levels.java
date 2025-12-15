package com.learnenglish.LearnEnglish.entity;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "levels")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Levels {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "created_at")
    private LocalDateTime created_at = LocalDateTime.now();
    @Column(name = "level_order", nullable = false)
    private Integer levelOrder;

    @OneToMany(mappedBy = "level")
    private Set<User> ListUser;

    @OneToMany(mappedBy = "level")
    private Set<Topics> ListTopics;

    @OneToMany(mappedBy = "level")
    private Set<User_level_progress> ListUserLevelProgress;

    @OneToMany(mappedBy = "level")
    private Set<Grammar> ListGrammar;

    @OneToMany(mappedBy = "currentLevel")
    private Set<User> ListUserCurrentLevel;
  
}

package com.learnenglish.LearnEnglish.entity;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "study_tracking")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Study_tracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(name = "study_date")
    private LocalDate studyDate;

    @Column(name = "minutes_spent")
    private int minutesSpent;
}

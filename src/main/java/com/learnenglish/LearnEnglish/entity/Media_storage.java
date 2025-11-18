package com.learnenglish.LearnEnglish.entity;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "media_storage")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Media_storage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "url")
    private String url;
    
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;
    
    @Column(name = "reference_table")
    private String reference_table;
    
    @Column(name = "reference_id")
    private Long reference_id;
    
    @Column(name = "created_at")
    private LocalDateTime created_at;
    
    public enum Type {
        IMAGE,
        AUDIO,
        VIDEO
    }
}

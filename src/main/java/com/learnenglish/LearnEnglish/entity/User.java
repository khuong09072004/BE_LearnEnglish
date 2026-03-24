package com.learnenglish.LearnEnglish.entity;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(nullable = false, unique = true, name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Column(name = "google_id")
    private String googleId;

    @Column(name = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @Column(name = "date_of_birth")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @ManyToOne
    @JoinColumn(name = "level_id", nullable = true)
    private Levels level;
        
    @ManyToOne
    @JoinColumn(name = "current_level_id")
    private Levels currentLevel;

    @Column(name = "has_selected_level")
    private Boolean hasSelectedLevel;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null)
            createdAt = LocalDateTime.now();
        if (updatedAt == null)
            updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum Role {
        USER, ADMIN
    }

    public enum Status {
        ACTIVE, LOCKED, PENDING
    }

    

    @OneToMany(mappedBy = "user")
    private Set<User_vocab_progress> ListUserV0caProgess;

    @OneToMany(mappedBy = "user")
    private Set<Exercise_results> ListExcerciseResults;



    @OneToMany(mappedBy = "user")
    private Set<Study_tracking> ListStudy_tracking;

    @OneToMany(mappedBy = "user")
    private Set<Reports> ListReports;

    @OneToMany(mappedBy = "user")
    private Set<Activity_logs> ListActivity_logs;

    @OneToMany(mappedBy = "user")
    private Set<OtpVerification> ListOTP;

    @OneToMany(mappedBy = "user")
    private Set<User_level_progress> ListUser_level_progress;

    @OneToMany(mappedBy = "user")
    private Set<Chat_messages> ListChatMessages;

    @OneToMany(mappedBy = "user")
    private Set<Notification> ListNotifications;
}

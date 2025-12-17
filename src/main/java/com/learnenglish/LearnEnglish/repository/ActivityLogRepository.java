package com.learnenglish.LearnEnglish.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learnenglish.LearnEnglish.entity.Activity_logs;
import com.learnenglish.LearnEnglish.entity.User;
@Repository
public interface ActivityLogRepository extends JpaRepository<Activity_logs, Long> {
     boolean existsByUserAndActionAndCreatedAtBetween(
        User user,
        String action,
        LocalDateTime start,
        LocalDateTime end
    );
}

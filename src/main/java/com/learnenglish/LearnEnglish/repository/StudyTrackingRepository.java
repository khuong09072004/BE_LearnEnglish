package com.learnenglish.LearnEnglish.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.learnenglish.LearnEnglish.entity.Study_tracking;
import com.learnenglish.LearnEnglish.entity.User;
@Repository
public interface StudyTrackingRepository extends JpaRepository<Study_tracking, Long> {

    Optional<Study_tracking> findByUserAndStudyDate(User user, LocalDate today);
    
    @Query("""
        SELECT COALESCE(SUM(st.minutesSpent), 0)
        FROM Study_tracking st
        WHERE st.user = :user
    """)
    int sumStudyMinutes(User user);
}

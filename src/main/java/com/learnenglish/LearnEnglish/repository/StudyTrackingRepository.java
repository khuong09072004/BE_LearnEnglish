package com.learnenglish.LearnEnglish.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("""
        SELECT st
        FROM Study_tracking st
        WHERE st.user = :user AND st.studyDate BETWEEN :startDate AND :endDate
        ORDER BY st.studyDate ASC
    """)
    List<Study_tracking> findByUserAndDateRange(
        @Param("user") User user,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    @Query("""
        SELECT COALESCE(SUM(st.minutesSpent), 0)
        FROM Study_tracking st
        WHERE st.user = :user AND st.studyDate BETWEEN :startDate AND :endDate
    """)
    int sumStudyMinutesByDateRange(
        @Param("user") User user,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}

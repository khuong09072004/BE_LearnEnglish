package com.learnenglish.LearnEnglish.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.learnenglish.LearnEnglish.entity.Exercise_results;
import com.learnenglish.LearnEnglish.entity.User;

@Repository
public interface ExerciseResultsRepository extends JpaRepository<Exercise_results,Long> {

    Optional<Exercise_results> findByExerciseIdAndUserId(Long exerciseId, Long userId);

    @Query("""
        SELECT COUNT(er)
        FROM Exercise_results er
        WHERE er.user = :user
          AND er.score >= 80
          AND er.exercise.topic.level.id = :levelId
    """)
    int countPassedExercisesByLevel(User user, Long levelId);

    @Query("""
        SELECT er
        FROM Exercise_results er
        WHERE er.user = :user
          AND CAST(er.completedAt AS LocalDate) BETWEEN :startDate AND :endDate
        ORDER BY er.completedAt DESC
    """)
    List<Exercise_results> findByUserAndDateRange(
        @Param("user") User user,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    @Query("""
        SELECT COUNT(er)
        FROM Exercise_results er
        WHERE er.user = :user
          AND er.score >= 80
          AND CAST(er.completedAt AS LocalDate) BETWEEN :startDate AND :endDate
    """)
    int countPassedExercisesByDateRange(
        @Param("user") User user,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
} 

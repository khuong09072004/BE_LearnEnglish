package com.learnenglish.LearnEnglish.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

} 

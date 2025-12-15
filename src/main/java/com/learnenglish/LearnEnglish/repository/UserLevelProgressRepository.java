package com.learnenglish.LearnEnglish.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.learnenglish.LearnEnglish.entity.User_level_progress;
@Repository
public interface UserLevelProgressRepository
        extends JpaRepository<User_level_progress, Long> {

    Optional<User_level_progress> findByUserIdAndLevelId(Long userId, Long levelId);

    boolean existsByUserIdAndLevelId(Long userId, Long levelId);


        @Query("""
        SELECT COALESCE(
            ROUND(
                SUM(CASE WHEN r.score >= 80 THEN 1 ELSE 0 END) * 100.0
                / NULLIF(COUNT(e), 0)
            ),
            0
        )
        FROM Exercises e
        JOIN e.topic t
        JOIN t.level l
        LEFT JOIN Exercise_results r
            ON r.exercise.id = e.id
            AND r.user.id = :userId
        WHERE l.id = :levelId
    """)
    Integer calculateLevelProgress(
        @Param("userId") Long userId,
        @Param("levelId") Long levelId
    );

}


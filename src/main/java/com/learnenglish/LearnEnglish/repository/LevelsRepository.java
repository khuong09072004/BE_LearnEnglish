package com.learnenglish.LearnEnglish.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.learnenglish.LearnEnglish.entity.Levels;

@Repository
public interface LevelsRepository extends JpaRepository<Levels, Long> {
    Optional<Levels> findByCode(String code);

    @Query("""
                SELECT l
                FROM Levels l
                LEFT JOIN l.ListUserLevelProgress ulp
                    WITH ulp.user.id = :userId
                WHERE ulp IS NULL OR ulp.is_completed = false
                ORDER BY l.levelOrder ASC
            """)
    List<Levels> findUncompletedLevels(@Param("userId") Long userId);

    List<Levels> findByLevelOrderLessThanEqualOrderByLevelOrder(
            Integer levelOrder);

    Optional<Levels> findByLevelOrder(Integer levelOrder);

    List<Levels> findByLevelOrderLessThan(Integer levelOrder);
}

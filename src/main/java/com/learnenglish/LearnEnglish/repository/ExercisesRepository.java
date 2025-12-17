package com.learnenglish.LearnEnglish.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.learnenglish.LearnEnglish.entity.Exercises;
import com.learnenglish.LearnEnglish.entity.Topics;
@Repository
public interface ExercisesRepository extends JpaRepository<Exercises,Long> {
    @Query("""
            select E
            from Exercises E
            where E.topic.id=:topicId
            """)
    List<Exercises> findByTopicId(@Param("topicId") Long topicId);
    
    List<Exercises> findByTopic(Topics topic);
    List<Exercises> findByTopicIdAndCategory(Long topicId, Exercises.ExerciseCategory category);

    List<Exercises> findByCategory(Exercises.ExerciseCategory category);
  @Query("""
    SELECT 
        e.id,
        e.topic.id,
        e.title,
        e.type,
        e.audioUrl,
        e.duration,
        e.category,
        r.score,
        CASE WHEN r.id IS NOT NULL THEN true ELSE false END,
        e.passingId
    FROM Exercises e
    LEFT JOIN Exercise_results r 
        ON r.exercise.id = e.id AND r.user.id = :userId
    WHERE e.topic.id = :topicId
""")
List<Object[]> findWithResultRaw(Long topicId, Long userId);

@Query("""
    SELECT e.id, e.topic.id, e.title, e.type, e.audioUrl, e.duration, e.category,
           r.score, CASE WHEN r.id IS NOT NULL THEN true ELSE false END,e.passingId
    FROM Exercises e
    LEFT JOIN Exercise_results r ON e.id = r.exercise.id AND r.user.id = :userId
    WHERE e.topic.id = :topicId AND e.category = :category
""")
List<Object[]> findByTopicCategoryRaw(Long topicId, Exercises.ExerciseCategory category, Long userId);

@Query("""
    SELECT COUNT(e)
    FROM Exercises e
    WHERE e.topic.level.id = :levelId
""")
int countByLevel(Long levelId);
}

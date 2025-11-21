package com.learnenglish.LearnEnglish.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.learnenglish.LearnEnglish.entity.Exercises;
@Repository
public interface ExercisesRepository extends JpaRepository<Exercises,Long> {
    @Query("""
            select E
            from Exercises E
            where E.topic.id=:topicId
            """)
    List<Exercises> findByTopicId(@Param("topicId") Long topicId);

}

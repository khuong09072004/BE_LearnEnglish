package com.learnenglish.LearnEnglish.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.learnenglish.LearnEnglish.entity.Vocabularies;
import com.learnenglish.LearnEnglish.entity.Topics;

@Repository
public interface VocabulariesRepository extends JpaRepository<Vocabularies,Long> {
    @Query(
            """
        SELECT v 
        FROM Vocabularies v
        WHERE v.topic.id = :topicId
        """)
    List<Vocabularies> findByTopicId(@Param("topicId") Long topicId);
    
    @Query("""
            select count(v) 
            from Vocabularies v
            WHERE v.topic.id = :topicId
            """)
    int countByTopicId(@Param ("topicId") Long topicId);

    @Query("""
        SELECT COUNT(v)
        FROM Vocabularies v
        WHERE v.topic.level.id = :levelId
    """)
    int countByLevel(Long levelId);

    @Query("""
        SELECT DISTINCT t
        FROM Topics t
        WHERE t.level.id = :levelId
        ORDER BY t.id
    """)
    List<Topics> findAllTopicsByLevel(@Param("levelId") Long levelId);
} 

package com.learnenglish.LearnEnglish.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.learnenglish.LearnEnglish.entity.Levels;
import com.learnenglish.LearnEnglish.entity.Topics;
import com.learnenglish.LearnEnglish.entity.imp.TopicSummary;
@Repository
public interface TopicsRepository extends JpaRepository<Topics,Long> {
      
    @Query("""
        SELECT t.id AS id, t.name AS name, t.level.code AS level, COUNT(v) AS totalVocab
        FROM Topics t
        LEFT JOIN t.vocabularies v
        WHERE t.level = :level
        GROUP BY t.id
    """)
    List<TopicSummary> findTopicSummariesByLevel(@Param("level") Levels level);

  
    @Query("""
        SELECT t.id AS id, t.name AS name, t.level.code AS level, COUNT(v) AS totalVocab
        FROM Topics t
        LEFT JOIN t.vocabularies v
        WHERE t.id = :id
        GROUP BY t.id
    """)
    TopicSummary findTopicSummaryById(@Param("id") Long id);

    @Query("""
        SELECT t.id AS id, t.name AS name, t.level.code AS level, COUNT(v) AS totalVocab
        FROM Topics t
        LEFT JOIN t.vocabularies v
        GROUP BY t.id
    """)
    List<TopicSummary> findAllTopicSummaries();
   
}

package com.learnenglish.LearnEnglish.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.learnenglish.LearnEnglish.entity.Conversations;

@Repository
public interface ConversationsRepository extends JpaRepository<Conversations,Long>{
    @Query(
            """
        SELECT C 
        FROM Conversations C
        WHERE C.topic.id = :topicId
        """)
    List<Conversations> findByTopic(@Param("topicId") Long topicId);
}

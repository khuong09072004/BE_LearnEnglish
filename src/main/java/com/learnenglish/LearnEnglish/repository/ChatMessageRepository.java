package com.learnenglish.LearnEnglish.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.learnenglish.LearnEnglish.entity.Chat_messages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ChatMessageRepository extends JpaRepository<Chat_messages, Long> {
    @Query("""
            SELECT m
            FROM Chat_messages m
            WHERE m.topic.id = :topicId
            ORDER BY m.sentAt DESC
            """)
    Page<Chat_messages> findByTopic(
            Long topicId,
            Pageable pageable);
}

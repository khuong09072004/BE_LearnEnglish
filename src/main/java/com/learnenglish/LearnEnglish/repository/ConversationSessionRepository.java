package com.learnenglish.LearnEnglish.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learnenglish.LearnEnglish.entity.ConversationSession;

@Repository
public interface ConversationSessionRepository extends JpaRepository<ConversationSession, Long> {
    
}

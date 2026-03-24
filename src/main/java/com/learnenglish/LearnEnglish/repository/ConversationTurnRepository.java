package com.learnenglish.LearnEnglish.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learnenglish.LearnEnglish.entity.ConversationTurn;
@Repository
public interface ConversationTurnRepository extends JpaRepository<ConversationTurn, Long> {
    List<ConversationTurn> findTop10BySessionIdOrderByCreatedAtAsc(Long sessionId);
}

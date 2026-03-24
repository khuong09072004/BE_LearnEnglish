package com.learnenglish.LearnEnglish.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learnenglish.LearnEnglish.entity.ConversationStepAttempt;
@Repository
public interface ConversationStepAttemptRepository extends JpaRepository<ConversationStepAttempt, Long> {
    Optional<ConversationStepAttempt> findBySessionIdAndStepId(Long sessionId, Long stepId);
}

package com.learnenglish.LearnEnglish.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learnenglish.LearnEnglish.entity.ConversationSession;

@Repository
public interface ConversationSessionRepository extends JpaRepository<ConversationSession, Long> {
	List<ConversationSession> findByUserIdOrderByStartedAtDesc(Long userId);
	List<ConversationSession> findByLessonIdOrderByStartedAtDesc(Long lessonId);
	List<ConversationSession> findAllByOrderByStartedAtDesc();
	long countByIsCompletedTrue();
	long countByLearnedTrue();
	java.util.Optional<ConversationSession> findTopByUserIdAndLessonIdAndLearnedTrueOrderByFinishedAtDesc(Long userId, Long lessonId);
	boolean existsByUserIdAndLessonIdAndLearnedTrue(Long userId, Long lessonId);
}

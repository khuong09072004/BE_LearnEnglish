package com.learnenglish.LearnEnglish.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learnenglish.LearnEnglish.entity.ConversationStep;
@Repository
public interface ConversationStepRepository extends JpaRepository<ConversationStep, Long> {
    Optional<ConversationStep> findByLessonIdAndStepOrder(Long lessonId, int stepOrder);

    boolean existsByLessonIdAndStepOrder(Long lessonId, int stepOrder);
    List<ConversationStep> findByLessonIdOrderByStepOrder(Long lessonId);
}

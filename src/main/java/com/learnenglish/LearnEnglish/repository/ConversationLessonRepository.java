package com.learnenglish.LearnEnglish.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.learnenglish.LearnEnglish.entity.ConversationLesson;
@Repository
public interface ConversationLessonRepository extends JpaRepository<ConversationLesson, Long> {
    
}
package com.learnenglish.LearnEnglish.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.learnenglish.LearnEnglish.entity.User_vocab_progress;

@Repository
public interface UserVocabProgressRepository extends JpaRepository<User_vocab_progress,Long> {
     Optional<User_vocab_progress> findByUserIdAndVocabularyId(Long userId, Long vocabId);

     @Query("""
               SELECT COUNT(uvp) 
               FROM User_vocab_progress uvp 
               WHERE uvp.user.id = :userId AND uvp.vocabulary.topic.id = :topicId AND uvp.isLearned = true
           """)
     int countLearnedByUserAndTopic(@Param("userId") Long userId, @Param("topicId") Long topicId);
}

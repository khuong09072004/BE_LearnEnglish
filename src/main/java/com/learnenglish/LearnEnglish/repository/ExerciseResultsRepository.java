package com.learnenglish.LearnEnglish.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learnenglish.LearnEnglish.entity.Exercise_results;

@Repository
public interface ExerciseResultsRepository extends JpaRepository<Exercise_results,Long> {

    Optional<Exercise_results> findByExerciseIdAndUserId(Long exerciseId, Long userId);

} 

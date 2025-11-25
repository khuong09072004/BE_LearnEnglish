package com.learnenglish.LearnEnglish.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learnenglish.LearnEnglish.entity.Exercise_results;

@Repository
public interface ExerciseResultsRepository extends JpaRepository<Exercise_results,Long> {

    
} 

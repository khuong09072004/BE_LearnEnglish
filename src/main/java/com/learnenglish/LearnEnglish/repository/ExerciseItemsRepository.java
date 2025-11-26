package com.learnenglish.LearnEnglish.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learnenglish.LearnEnglish.entity.ExerciseItems;
import com.learnenglish.LearnEnglish.entity.Exercises;
@Repository
public interface ExerciseItemsRepository extends JpaRepository<ExerciseItems,Long> {
    List<ExerciseItems> findByExerciseIdOrderByPosition(Long exerciseId);

    List<ExerciseItems> findByExercise (Exercises exercises );
}

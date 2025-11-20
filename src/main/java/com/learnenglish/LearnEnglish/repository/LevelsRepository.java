package com.learnenglish.LearnEnglish.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learnenglish.LearnEnglish.entity.Levels;
@Repository
public interface LevelsRepository extends JpaRepository<Levels,Long> {
    Optional<Levels> findByCode(String code);
}

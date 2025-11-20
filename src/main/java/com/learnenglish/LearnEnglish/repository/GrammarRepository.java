package com.learnenglish.LearnEnglish.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learnenglish.LearnEnglish.entity.Grammar;
import com.learnenglish.LearnEnglish.entity.Levels;
@Repository
public interface GrammarRepository extends JpaRepository<Grammar,Long> {
    List<Grammar> findByLevel(Levels level);

    Optional<Grammar> findById(Long id);
}

package com.learnenglish.LearnEnglish.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.learnenglish.LearnEnglish.entity.Levels;
import com.learnenglish.LearnEnglish.entity.Topics;
@Repository
public interface TopicsRepository extends JpaRepository<Topics,Long> {
    List<Topics> findByLevel(Levels level);
   
}

package com.learnenglish.LearnEnglish.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learnenglish.LearnEnglish.entity.Topics;
@Repository
public interface TopicsRepository extends JpaRepository<Topics,Long> {
    List<Topics> findByLevel(String level);
}

package com.learnenglish.LearnEnglish.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learnenglish.LearnEnglish.entity.Passages;
@Repository
public interface PassagesRepository extends JpaRepository<Passages,Long> {
    
}

package com.learnenglish.LearnEnglish.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnenglish.LearnEnglish.entity.Passages;
import com.learnenglish.LearnEnglish.repository.PassagesRepository;

import jakarta.validation.ValidationException;

@Service
public class PassagesService {
    @Autowired
    PassagesRepository passagesRepository;

    public Passages getPassagesById(Long id)
    {
        Passages passages=passagesRepository.findById(id)
        .orElseThrow(()->new ValidationException("Bài đọc không tồn tại"));
        return passages;
    }
}

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

    public Passages createPassage(String title, String content, String categoryStr) {
        Passages p = new Passages();
        p.setTitle(title);
        p.setContent(content);
        if (categoryStr != null) {
            try {
                p.setCategory(Passages.Category.valueOf(categoryStr.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new ValidationException("Category không hợp lệ: " + categoryStr);
            }
        } else {
            p.setCategory(Passages.Category.READING);
        }

        return passagesRepository.save(p);
    }

    public Passages updatePassage(Long id, String title, String content, String categoryStr) {
        Passages p = passagesRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Bài đọc không tồn tại"));

        if (title != null)
            p.setTitle(title);
        if (content != null)
            p.setContent(content);
        if (categoryStr != null) {
            try {
                p.setCategory(Passages.Category.valueOf(categoryStr.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new ValidationException("Category không hợp lệ: " + categoryStr);
            }
        }

        return passagesRepository.save(p);
    }

    public void deletePassage(Long id) {
        Passages p = passagesRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Bài đọc không tồn tại"));
        passagesRepository.delete(p);
    }

    public java.util.List<Passages> listAll() {
        return passagesRepository.findAll();
    }
}

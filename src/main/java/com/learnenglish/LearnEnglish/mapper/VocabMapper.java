package com.learnenglish.LearnEnglish.mapper;

import org.springframework.stereotype.Component;

import com.learnenglish.LearnEnglish.dto.responses.VocaBularyRespone;
import com.learnenglish.LearnEnglish.entity.Vocabularies;

@Component
public class VocabMapper {
    public VocaBularyRespone toDTO(Vocabularies vocab, boolean isLearned) {
        return new VocaBularyRespone(
            vocab.getId(),
            vocab.getTopic().getId(),
            vocab.getWord(),
            vocab.getMeaning(),
            vocab.getPhonetic(),
            vocab.getDescription(),
            vocab.getImage_url(),
            isLearned
        );
    }
}

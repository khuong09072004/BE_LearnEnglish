package com.learnenglish.LearnEnglish.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.learnenglish.LearnEnglish.dto.responses.VocaBularyRespone;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.entity.User_vocab_progress;
import com.learnenglish.LearnEnglish.entity.Vocabularies;
import com.learnenglish.LearnEnglish.repository.UserVocabProgressRepository;

@Component
public class VocabMapper {
    @Autowired
    UserVocabProgressRepository userVocabProgressRepository;

    public VocaBularyRespone toDTO(Vocabularies vocab, boolean isLearned) {
        return new VocaBularyRespone(
                vocab.getId(),
                vocab.getTopic().getId(),
                vocab.getWord(),
                vocab.getMeaning(),
                vocab.getPhonetic(),
                vocab.getDescription(),
                vocab.getImage_url(),
                isLearned);
    }

    public List<VocaBularyRespone> toListDTO(List<Vocabularies> lst, User user) {
        List<VocaBularyRespone> respones = new ArrayList<>();
        for (Vocabularies item : lst) {
            boolean is_learned = false;

            if (user != null) {
                is_learned = userVocabProgressRepository
                        .findByUserIdAndVocabularyId(user.getId(), item.getId())
                        .map(User_vocab_progress::isLearned)
                        .orElse(false);
            }

            VocaBularyRespone dto = toDTO(item, is_learned);
            respones.add(dto);
        }
        return respones;
    }
}

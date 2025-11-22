package com.learnenglish.LearnEnglish.mapper;

import org.springframework.stereotype.Component;

import com.learnenglish.LearnEnglish.dto.responses.GrammarRespone;
import com.learnenglish.LearnEnglish.entity.Grammar;

@Component
public class GrammarMapper {
   public GrammarRespone toDTO(Grammar item)
    {
        return new GrammarRespone(item.getId(), item.getTitle(), item.getContent(), item.getExample(), item.getSource(), item.getLevel().getCode());
    }
}

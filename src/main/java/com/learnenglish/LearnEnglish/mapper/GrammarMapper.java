package com.learnenglish.LearnEnglish.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.learnenglish.LearnEnglish.dto.responses.GrammarRespone;
import com.learnenglish.LearnEnglish.entity.Grammar;

@Component
public class GrammarMapper {
   public GrammarRespone toDTO(Grammar item)
    {
        return new GrammarRespone(item.getId(), item.getTitle(), item.getContent(), item.getExample(), item.getSource(), item.getLevel().getCode());
    }

    public List<GrammarRespone> toListDTO(List<Grammar> lst)
    {
        List<GrammarRespone> respones=new ArrayList<>();
        for(Grammar item : lst)
        {
            GrammarRespone dto=toDTO(item);
            respones.add(dto);
        }
        return respones;
    }
}

package com.learnenglish.LearnEnglish.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnenglish.LearnEnglish.dto.responses.GrammarRespone;
import com.learnenglish.LearnEnglish.entity.Grammar;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.exception.ValidationException;
import com.learnenglish.LearnEnglish.mapper.GrammarMapper;
import com.learnenglish.LearnEnglish.repository.GrammarRepository;
import com.learnenglish.LearnEnglish.repository.UserRepository;

@Service
public class GrammarService {
    @Autowired
    GrammarRepository grammarRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GrammarMapper grammarMapper;
    private List<GrammarRespone> mapToRespone(List<Grammar> lst)
    {
        List<GrammarRespone> respones=new ArrayList<>();
        for(Grammar item : lst)
        {
            GrammarRespone dto=grammarMapper.toDTO(item);
            respones.add(dto);
        }
        return respones;
    }

    public List<GrammarRespone> getGrammars(String email)
    {
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Không tìm thấy tài khoản với email này"));
            List<Grammar> grammars=grammarRepository.findByLevel(user.getLevel());
            return mapToRespone(grammars);
    }

    public GrammarRespone getGrammarByid(String email,Long id)
    {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Không tìm thấy tài khoản với email này"));
        Grammar grammar=grammarRepository.findById(id).orElseThrow(()-> new ValidationException("Không tìm thấy grammar"));
        if(!grammar.getLevel().getCode().equals(user.getLevel().getCode()))
        {
            throw new ValidationException("Grammar không thuộc trình độ này");
        }

        return grammarMapper.toDTO(grammar);
    }
}

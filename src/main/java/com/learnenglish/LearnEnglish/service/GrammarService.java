package com.learnenglish.LearnEnglish.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.checkerframework.checker.units.qual.g;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnenglish.LearnEnglish.dto.requests.GrammarRequest;
import com.learnenglish.LearnEnglish.dto.responses.GrammarRespone;
import com.learnenglish.LearnEnglish.entity.Grammar;
import com.learnenglish.LearnEnglish.entity.Levels;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.exception.ValidationException;
import com.learnenglish.LearnEnglish.mapper.GrammarMapper;
import com.learnenglish.LearnEnglish.repository.GrammarRepository;
import com.learnenglish.LearnEnglish.repository.LevelsRepository;
import com.learnenglish.LearnEnglish.repository.UserRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Validation;

@Service
public class GrammarService {
    @Autowired
    GrammarRepository grammarRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GrammarMapper grammarMapper;
    @Autowired
    LevelsRepository levelsRepository;
    

    public List<GrammarRespone> getGrammars(String email)
    {
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Không tìm thấy tài khoản với email này"));
            List<Grammar> grammars=grammarRepository.findByLevel(user.getLevel());
            return grammarMapper.toListDTO(grammars);
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

    @Transactional
    public GrammarRespone createGrammarByAdmin(GrammarRequest request)
    {
        Levels level=levelsRepository.findById(request.getLevelId())
            .orElseThrow(()-> new ValidationException("Level Không tồn tại"));
        Grammar grammar=new Grammar();
        grammar.setLevel(level);
        grammar.setTitle(request.getTitle());
        grammar.setContent(request.getContent());
        grammar.setExample(request.getExample());
        grammar.setSource(request.getSource());
        grammarRepository.save(grammar);
        return grammarMapper.toDTO(grammar);

    }

    @Transactional
    public GrammarRespone updateGrammarByAdmin(Long gramarId,GrammarRequest request)
    {
        Levels level=levelsRepository.findById(request.getLevelId())
            .orElseThrow(()-> new ValidationException("Level Không tồn tại"));
        Grammar grammar=grammarRepository.findById(gramarId).
                    orElseThrow(()-> new ValidationException("Không tìm thấy Grammar"));
        grammar.setLevel(level);
        grammar.setTitle(request.getTitle());
        grammar.setContent(request.getContent());
        grammar.setExample(request.getExample());
        grammar.setSource(request.getSource());
        grammarRepository.save(grammar);
        return grammarMapper.toDTO(grammar);
    }

    @Transactional
    public GrammarRespone deleteGrammarByAdmin(Long gramarId)
    {
        Grammar grammar=grammarRepository.findById(gramarId).
                    orElseThrow(()-> new ValidationException("Không tìm thấy Grammar"));
        grammarRepository.delete(grammar);
        return grammarMapper.toDTO(grammar);
    }
}

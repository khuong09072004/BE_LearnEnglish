package com.learnenglish.LearnEnglish.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnenglish.LearnEnglish.dto.responses.ExercisesRespone;
import com.learnenglish.LearnEnglish.entity.Exercises;
import com.learnenglish.LearnEnglish.entity.Topics;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.exception.ValidationException;
import com.learnenglish.LearnEnglish.mapper.ExerciesMapper;
import com.learnenglish.LearnEnglish.repository.ExercisesRepository;
import com.learnenglish.LearnEnglish.repository.TopicsRepository;
import com.learnenglish.LearnEnglish.repository.UserRepository;

@Service
public class ExercisesService {
    @Autowired
    ExercisesRepository exercisesRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TopicsRepository topicsRepository;
    @Autowired
    ExerciesMapper exerciesMapper;

    private List<ExercisesRespone> mapToRespones(List<Exercises> lst)
    {
        List<ExercisesRespone> respones=new ArrayList<>();
        for(Exercises item : lst)
        {
            ExercisesRespone dto=exerciesMapper.toDTO(item);
            respones.add(dto);
        }
        return respones;
    }

    public List<ExercisesRespone> getExercies(String email,Long topicId)
    {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Không tìm thấy tài khoản với email này"));
        Topics topic = topicsRepository.findById(topicId)
                .orElseThrow(() -> new ValidationException("Không tìm thấy Topics "));
        if(!user.getLevel().getCode().equals(topic.getLevel().getCode()))
        {
            throw new ValidationException("Topic không phù hợp với trình độ của người học");
        }  
        List<Exercises> respones=exercisesRepository.findByTopicId(topicId);
        return mapToRespones(respones);  
    }

    public ExercisesRespone getExerciesById(Long id)
    {
        Exercises respone=exercisesRepository.findById(id)
        .orElseThrow(() -> new ValidationException("Không tìm thấy Exercises"));
        return exerciesMapper.toDTO(respone);
    }
}

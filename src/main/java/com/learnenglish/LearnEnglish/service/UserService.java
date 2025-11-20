package com.learnenglish.LearnEnglish.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnenglish.LearnEnglish.dto.responses.TopicsRespone;
import com.learnenglish.LearnEnglish.entity.Levels;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.exception.ValidationException;
import com.learnenglish.LearnEnglish.repository.LevelsRepository;
import com.learnenglish.LearnEnglish.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    LevelsRepository levelsRepository;
    public void SelectLevel(String level,String email)
    {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Không tìm thấy tài khoản với email này"));
        Levels Userlevel=levelsRepository.findByCode(level)
            .orElseThrow(()-> new ValidationException("Level không hợp lệ"));


        user.setLevel(Userlevel);
        user.setHasSelectedLevel(true);

        userRepository.save(user);
    }

    
}

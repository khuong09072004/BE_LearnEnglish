package com.learnenglish.LearnEnglish.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnenglish.LearnEnglish.dto.responses.TopicsRespone;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.exception.ValidationException;
import com.learnenglish.LearnEnglish.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public void SelectLevel(String level,String email)
    {
          User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Không tìm thấy tài khoản với email này"));
           User.UserLevel userLevelEnum;

        try {
            userLevelEnum = User.UserLevel.valueOf(level.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException(
                "Level không hợp lệ! Các level hợp lệ: BEGINNER, A1, A2, B1, B2, C1, C2"
            );
        }

        user.setUserLevel(userLevelEnum);
        user.setHasSelectedLevel(true);

        userRepository.save(user);
    }

    
}

package com.learnenglish.LearnEnglish.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnenglish.LearnEnglish.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
}

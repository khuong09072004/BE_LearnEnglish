package com.learnenglish.LearnEnglish.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnenglish.LearnEnglish.dto.enums.ActivityAction;
import com.learnenglish.LearnEnglish.entity.Activity_logs;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.repository.ActivityLogRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class ActivityLogService {
    @Autowired
    ActivityLogRepository activityLogRepository;

    public void log(User user, ActivityAction action, String detailJson) {
        Activity_logs log = new Activity_logs();
        log.setUser(user);
        log.setAction(action.name());
        log.setDetail(detailJson);
        log.setCreatedAt(LocalDateTime.now());

        activityLogRepository.save(log);
    }

    public void log(User user, ActivityAction action) {
        log(user, action, null);
    }
}

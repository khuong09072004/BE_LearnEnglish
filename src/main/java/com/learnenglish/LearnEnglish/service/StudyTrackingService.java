package com.learnenglish.LearnEnglish.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnenglish.LearnEnglish.entity.Study_tracking;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.repository.StudyTrackingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudyTrackingService {
    @Autowired
    StudyTrackingRepository studyTrackingRepository;
    public void addStudyMinutes(User user, int minutes) {
        LocalDate today = LocalDate.now();

        Study_tracking tracking = studyTrackingRepository
                .findByUserAndStudyDate(user, today)
                .orElseGet(() -> {
                    Study_tracking st = new Study_tracking();
                    st.setUser(user);
                    st.setStudyDate(today);
                    st.setMinutesSpent(0);
                    return st;
                });

        tracking.setMinutesSpent(tracking.getMinutesSpent() + minutes);
        studyTrackingRepository.save(tracking);
    }
}

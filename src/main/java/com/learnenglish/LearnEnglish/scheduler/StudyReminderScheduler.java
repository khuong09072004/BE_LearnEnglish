package com.learnenglish.LearnEnglish.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.learnenglish.LearnEnglish.dto.enums.ActivityAction;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.repository.UserRepository;
import com.learnenglish.LearnEnglish.service.NotificationService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class StudyReminderScheduler {
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  NotificationService notificationService;

     @Scheduled(cron = "0 0 9 * * ?")
    public void remindIfNotStudy3Days() {

        log.info("⏰ Running Study Reminder Job");

        LocalDateTime since = LocalDateTime.now().minusDays(3);

        List<ActivityAction> actions = List.of(
            ActivityAction.COMPLETE_EXERCISE,
            ActivityAction.COMPLETE_TOPIC,
            ActivityAction.COMPLETE_CONVERSATION,
            ActivityAction.LEVEL_UP
        );

        List<User> users = userRepository.findUsersNotStudySince(actions, since);

        for (User user : users) {
            notificationService.sendStudyReminder(user);
        }
    }
}

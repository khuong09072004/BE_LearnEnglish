package com.learnenglish.LearnEnglish.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${app.study.reminder.seconds:259200}")
    private long reminderThresholdSeconds; // default 3 days

    @Scheduled(cron = "0 0 9 * * ?")
    public void remindIfNotStudy3Days() {
        runReminderJob();
    }

    // public helper to allow manual triggering (e.g., from test controller)
    public void runReminderJob() {
        log.info("⏰ Running Study Reminder Job");

        LocalDateTime since = LocalDateTime.now().minusSeconds(reminderThresholdSeconds);

        List<ActivityAction> actions = List.of(
            ActivityAction.COMPLETE_EXERCISE,
            ActivityAction.COMPLETE_TOPIC,
            ActivityAction.COMPLETE_CONVERSATION,
            ActivityAction.LEVEL_UP
        );

        List<String> actionNames = actions.stream().map(ActivityAction::name).toList();

        List<User> users = userRepository.findUsersNotStudySince(actionNames, since);

        for (User user : users) {
            notificationService.sendStudyReminder(user);
        }
    }
}

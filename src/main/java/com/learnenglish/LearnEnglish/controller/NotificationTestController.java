package com.learnenglish.LearnEnglish.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.repository.UserRepository;
import com.learnenglish.LearnEnglish.service.NotificationService;



@RestController
@RequestMapping("/api/test/notifications")
public class NotificationTestController {
    @Autowired
    private  NotificationService notificationService;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private com.learnenglish.LearnEnglish.scheduler.StudyReminderScheduler studyReminderScheduler;

    // Test STUDY REMINDER
    @PostMapping("/study-reminder/{userId}")
    public String testStudyReminder(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        notificationService.sendStudyReminder(user);
        return "OK - STUDY_REMINDER sent";
    }

    //  Test LEVEL COMPLETED
    @PostMapping("/level-completed/{userId}")
    public String testLevelCompleted(
        @PathVariable Long userId,
        @RequestParam String level
    ) {
        User user = userRepository.findById(userId).orElseThrow();
        notificationService.sendLevelCompleted(user, level);
        return "OK - LEVEL_COMPLETED sent";
    }

    //  Test SYSTEM
    @PostMapping("/system/{userId}")
    public String testSystem(
        @PathVariable Long userId
    ) {
        User user = userRepository.findById(userId).orElseThrow();
        notificationService.sendSystemNotification(
            user,
            "📢 Thông báo hệ thống",
            "Hệ thống sẽ bảo trì lúc 23:00 hôm nay",
            """
            {
              "action": "OPEN_SYSTEM_NOTICE"
            }
            """
        );
        return "OK - SYSTEM sent";
    }

    // Run the reminder job immediately (for testing). You can set property
    // `app.study.reminder.seconds` to a small value (e.g. 10) to test threshold.
    @PostMapping("/run-reminder")
    public String runReminderNow() {
        studyReminderScheduler.runReminderJob();
        return "OK - reminder job executed";
    }
}
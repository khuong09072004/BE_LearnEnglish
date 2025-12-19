package com.learnenglish.LearnEnglish.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.learnenglish.LearnEnglish.dto.enums.NotificationType;
import com.learnenglish.LearnEnglish.dto.responses.NotificationResponse;
import com.learnenglish.LearnEnglish.dto.responses.NotificationSocketResponse;
import com.learnenglish.LearnEnglish.entity.Notification;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.repository.NotificationRepository;
import com.learnenglish.LearnEnglish.security.CustomUserPrincipal;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NotificationService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private NotificationRepository notificationRepository;

    private void send(User user, Notification notification) {

        Notification saved = notificationRepository.save(notification);
        NotificationSocketResponse response = NotificationSocketResponse.builder()
                .id(saved.getId())
                .type(saved.getType())
                .title(saved.getTitle())
                .content(saved.getContent())
                .data(saved.getData())
                .createdAt(saved.getCreatedAt())
                .build();
        messagingTemplate.convertAndSendToUser(
                user.getId().toString(),
                "/queue/notifications",
                response);
    }

    public void sendStudyReminder(User user) {

        Notification notification = Notification.builder()
                .user(user)
                .type(NotificationType.STUDY_REMINDER)
                .title("📚 Bạn đã 3 ngày chưa học")
                .content("Chỉ 10 phút mỗi ngày cũng đủ để duy trì thói quen học tập!")
                .data("""
                        {
                          "days": 3,
                          "action": "OPEN_LEARNING_HOME"
                        }
                        """)
                .createdAt(LocalDateTime.now())
                .build();

        send(user, notification);
    }

    public void sendLevelCompleted(User user, String levelCode) {

        Notification notification = Notification.builder()
                .user(user)
                .type(NotificationType.LEVEL_COMPLETED)
                .title("🎉 Hoàn thành level " + levelCode)
                .content("Chúc mừng bạn đã hoàn thành level " + levelCode + "! Tiếp tục chinh phục level mới nhé 🚀")
                .data("""
                        {
                          "level": "%s",
                          "action": "OPEN_LEVEL_RESULT"
                        }
                        """.formatted(levelCode))
                .createdAt(LocalDateTime.now())
                .build();

        send(user, notification);
    }

    public void sendSystemNotification(
            User user,
            String title,
            String content,
            String dataJson) {

        Notification notification = Notification.builder()
                .user(user)
                .type(NotificationType.SYSTEM)
                .title(title)
                .content(content)
                .data(dataJson)
                .createdAt(LocalDateTime.now())
                .build();

        send(user, notification);
    }

    private Long currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof CustomUserPrincipal userPrincipal) {
            return userPrincipal.getId();
        }

        throw new IllegalStateException("Principal is not CustomUserPrincipal");
    }

    // Query notifications
    public Page<NotificationResponse> getMyNotifications(Pageable pageable) {

        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(
                        currentUserId(),
                        pageable)
                .map(this::toResponse);
    }

    public void markAsRead(Long notificationId) {

        Notification n = notificationRepository
                .findByIdAndUserId(notificationId, currentUserId())
                .orElseThrow(() -> new RuntimeException("Notification không tồn tại"));

        if (!n.getIsRead()) {
            n.setIsRead(true);
        }
    }

    public void markAllAsRead() {
        notificationRepository.markAllAsRead(currentUserId());
    }

    public long getUnreadCount() {
        return notificationRepository.countUnreadByUserId(
                currentUserId());
    }

    public void deleteById(Long notificationId) {
        Long userId = currentUserId();

        int deleted = notificationRepository
            .deleteByIdAndUserId(notificationId, userId);

        if (deleted == 0) {
            throw new RuntimeException("Notification không tồn tại hoặc không thuộc user");
        }
    }

    public void deleteAll() {
        Long userId = currentUserId();
        notificationRepository.deleteAllByUserId(userId);
    }


    private NotificationResponse toResponse(Notification n) {

        return NotificationResponse.builder()
                .id(n.getId())
                .type(n.getType())
                .title(n.getTitle())
                .content(n.getContent())
                .data(n.getData())
                .read(n.getIsRead())
                .createdAt(n.getCreatedAt())
                .build();
    }
}

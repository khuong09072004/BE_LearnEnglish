package com.learnenglish.LearnEnglish.dto.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardResponse {
    private long totalUsers;
    private long activeUsers;
    private long lockedUsers;
    private long pendingUsers;
    private long adminUsers;

    private long totalLevels;
    private long totalTopics;
    private long totalGrammar;
    private long totalVocabularies;
    private long totalExercises;

    private long totalConversationLessons;
    private long totalConversationSessions;
    private long completedConversationSessions;
    private long learnedConversationSessions;

    private long recentRegisteredUsersCount;
    private List<UserResponse> recentRegisteredUsers;
}
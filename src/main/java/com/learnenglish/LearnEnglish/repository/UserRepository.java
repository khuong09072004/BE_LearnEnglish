package com.learnenglish.LearnEnglish.repository;

import com.learnenglish.LearnEnglish.dto.enums.ActivityAction;
import com.learnenglish.LearnEnglish.entity.Topics;
import com.learnenglish.LearnEnglish.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("""
            SELECT u
            FROM User u
            WHERE u.status = 'ACTIVE'
            AND NOT EXISTS (
                SELECT 1
                FROM Activity_logs a
                WHERE a.user = u
                AND a.action IN :actions
                AND a.createdAt >= :since
            )
            """)
    List<User> findUsersNotStudySince(
            @Param("actions") List<ActivityAction> actions,
            @Param("since") LocalDateTime since);

}

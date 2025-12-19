package com.learnenglish.LearnEnglish.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.learnenglish.LearnEnglish.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByUserIdOrderByCreatedAtDesc(
            Long userId,
            Pageable pageable);

    @Query("""
                select count(n)
                from Notification n
                where n.user.id = :userId
                and n.isRead = false
            """)
    long countUnreadByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("""
                update Notification n
                set n.isRead = true
                where n.user.id = :userId
                  and n.isRead = false
            """)
    void markAllAsRead(@Param("userId") Long userId);

    @Query("""
                select n from Notification n
                where n.id = :id and n.user.id = :userId
            """)
    Optional<Notification> findByIdAndUserId(
            @Param("id") Long id,
            @Param("userId") Long userId);

    
@Modifying
    @Query("""
        delete from Notification n
        where n.id = :id
          and n.user.id = :userId
    """)
    int deleteByIdAndUserId(
        @Param("id") Long id,
        @Param("userId") Long userId
    );

    @Modifying
    @Query("""
        delete from Notification n
        where n.user.id = :userId
    """)
    void deleteAllByUserId(
        @Param("userId") Long userId
    );
}

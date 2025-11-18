package com.learnenglish.LearnEnglish.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.learnenglish.LearnEnglish.entity.OtpVerification;

@Repository
public interface OtpRepository extends JpaRepository<OtpVerification, Long> {
    @Query("SELECT COUNT(o) FROM OtpVerification o WHERE o.user.id = :userId AND o.type = :type AND o.createdAt BETWEEN :startOfDay AND :endOfDay")
    int countOtpSentToday(Long userId, String type, LocalDateTime startOfDay, LocalDateTime endOfDay);

    Optional<OtpVerification> findFirstByUserIdAndTypeOrderByCreatedAtDesc(Long userId, String type);

    Optional<OtpVerification> findByUserIdAndOtpCodeAndVerifiedFalse(Long userId, String otpCode);

    Optional<OtpVerification> findByUserIdAndTypeAndOtpCodeAndVerifiedFalse(Long userId, String type, String otpCode);

    Optional<OtpVerification> findByUserIdAndTypeAndOtpCodeAndVerifiedTrue(Long userId, String type, String otpCode);
}

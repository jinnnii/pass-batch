package com.jinnnii.pass.repository;

import com.jinnnii.pass.domain.BulkNotificationEntity;
import com.jinnnii.pass.domain.constant.BulkStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BulkNotificationRepository extends JpaRepository<BulkNotificationEntity, Long> {
    List<BulkNotificationEntity> findByStatusAndBookedAtLessThan(BulkStatus status, LocalDateTime bookedAt);
}
package com.jinnnii.pass.repository;

import com.jinnnii.pass.domain.BulkPassEntity;
import com.jinnnii.pass.domain.constant.BulkStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BulkPassRepository extends JpaRepository<BulkPassEntity, Long> {

    // status = :status and startedAt > :startedAt
    List<BulkPassEntity> findByStatusAndStartedAtGreaterThan(BulkStatus ready, LocalDateTime now);
}

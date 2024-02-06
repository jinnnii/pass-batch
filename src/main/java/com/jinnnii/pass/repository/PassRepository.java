package com.jinnnii.pass.repository;

import com.jinnnii.pass.domain.PassEntity;
import com.jinnnii.pass.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface PassRepository extends JpaRepository<PassEntity, Long> {
    @Query(value = "select distinct u from PassEntity p" +
            " join  p.userEntity u" +
            " where p.placeEntity.placeId = :placeId" +
            " and (p.expiredAt is null or p.expiredAt >= :expiredAt)")
    Set<UserEntity> findValidUserByPlaceId(Long placeId, LocalDateTime expiredAt);
}

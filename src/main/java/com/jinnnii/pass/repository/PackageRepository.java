package com.jinnnii.pass.repository;

import com.jinnnii.pass.domain.PackageEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface PackageRepository extends JpaRepository<PackageEntity, Long> {
    List<PackageEntity> findByCreatedAtAfter(LocalDateTime dateTime, Pageable pageable);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value=
        "update PackageEntity p " +
        "   set p.period = :period," +
        "       p.price = :price " +
        "where p.packageId = :packageId")
    int updatePeriodAndPrice(Long packageId, Integer period, Integer price);
}

package com.jinnnii.pass.repository;

import com.jinnnii.pass.domain.PassEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassRepository extends JpaRepository<PassEntity, Long> {
}

package com.jinnnii.pass.repository;

import com.jinnnii.pass.domain.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageRepository extends JpaRepository<PackageEntity, String> {
}

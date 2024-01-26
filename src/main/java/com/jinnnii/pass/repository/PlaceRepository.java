package com.jinnnii.pass.repository;

import com.jinnnii.pass.domain.PlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<PlaceEntity, String> {
}

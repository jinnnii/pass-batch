package com.jinnnii.pass.repository;

import com.jinnnii.pass.domain.UserGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupRepository extends JpaRepository<UserGroupEntity, Long> {
}
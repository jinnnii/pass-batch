package com.jinnnii.pass.repository;

import com.jinnnii.pass.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
}

package com.user.project.User.Project.infrastructure.repository;

import com.user.project.User.Project.infrastructure.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
}

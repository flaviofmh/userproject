package com.user.project.User.Project.infrastructure.repository;

import com.user.project.User.Project.infrastructure.entity.UserProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProjectEntityRepository extends JpaRepository<UserProjectEntity, Long> {
}

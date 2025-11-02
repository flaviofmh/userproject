package com.user.project.User.Project.infrastructure.repository;

import com.user.project.User.Project.infrastructure.entity.UserProjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProjectEntityRepository extends JpaRepository<UserProjectEntity, Long> {

    Page<UserProjectEntity> findAllByUserId(Long userId, Pageable pageable);

}

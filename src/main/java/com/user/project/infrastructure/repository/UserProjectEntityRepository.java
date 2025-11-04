package com.user.project.infrastructure.repository;

import com.user.project.infrastructure.entity.UserProjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProjectEntityRepository extends JpaRepository<UserProjectEntity, Long> {

    Page<UserProjectEntity> findAllByUserId(Long userId, Pageable pageable);

}

package com.user.project.User.Project.domain.repository;

import com.user.project.User.Project.domain.model.UserProjects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserProjectGateway {

    UserProjects save(UserProjects userProjects);
    Page<UserProjects> findAllByUserId(Long id, Pageable pageable);

}

package com.user.project.domain.usecase;

import com.user.project.domain.model.UserProjects;
import com.user.project.domain.repository.UserProjectGateway;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class UserProjectRetrievalUseCase {

    private final UserProjectGateway userProjectGateway;

    public UserProjectRetrievalUseCase(UserProjectGateway userProjectGateway) {
        this.userProjectGateway = userProjectGateway;
    }

    public Page<UserProjects> retrieveProjectsByUserId(Long userId, Pageable pageable) {
        return userProjectGateway.findAllByUserId(userId, pageable);
    }

}

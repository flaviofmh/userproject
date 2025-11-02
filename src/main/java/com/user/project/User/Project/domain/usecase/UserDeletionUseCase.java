package com.user.project.User.Project.domain.usecase;

import com.user.project.User.Project.domain.repository.UserGateway;

public class UserDeletionUseCase {

    private final UserGateway userGateway;

    public UserDeletionUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public void execute(Long userId) {
        userGateway.deleteById(userId);
    }

}

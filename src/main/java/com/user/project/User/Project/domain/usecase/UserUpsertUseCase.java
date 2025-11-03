package com.user.project.User.Project.domain.usecase;

import com.user.project.User.Project.domain.model.User;
import com.user.project.User.Project.domain.repository.UserGateway;

public class UserUpsertUseCase {

    private final UserGateway userGateway;

    public UserUpsertUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public User execute(User user) {
        return userGateway.save(user);
    }
}

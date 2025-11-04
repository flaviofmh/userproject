package com.user.project.domain.usecase;

import com.user.project.domain.model.User;
import com.user.project.domain.repository.UserGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserUpsertUseCase {

    private static final Logger logger = LoggerFactory.getLogger(UserUpsertUseCase.class);
    private final UserGateway userGateway;

    public UserUpsertUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public User execute(User user) {
        logger.info("Executing UserUpsertUseCase for user: {}", user);
        return userGateway.save(user);
    }
}

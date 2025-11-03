package com.user.project.User.Project.domain.usecase;

import com.user.project.User.Project.domain.repository.UserGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDeletionUseCase {

    private static final Logger logger = LoggerFactory.getLogger(UserDeletionUseCase.class);
    private final UserGateway userGateway;

    public UserDeletionUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public void execute(Long userId) {
        logger.info("Executing UserDeletionUseCase with userId: {}", userId);
        userGateway.deleteById(userId);
        logger.info("User with userId {} deleted successfully", userId);
    }

}

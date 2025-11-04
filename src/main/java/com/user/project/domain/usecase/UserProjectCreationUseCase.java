package com.user.project.domain.usecase;

import com.user.project.domain.exception.EntityNotFoundException;
import com.user.project.domain.exception.InvalidUserProjectException;
import com.user.project.domain.model.UserProjects;
import com.user.project.domain.repository.UserGateway;
import com.user.project.domain.repository.UserProjectGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserProjectCreationUseCase {

    private static final Logger logger = LoggerFactory.getLogger(UserProjectCreationUseCase.class);
    private final UserGateway userGateway;
    private final UserProjectGateway userProjectGateway;

    public UserProjectCreationUseCase(UserProjectGateway userProjectGateway, UserGateway userGateway) {
        this.userGateway = userGateway;
        this.userProjectGateway = userProjectGateway;
    }

    public UserProjects execute(UserProjects userProjects) {
        if (userProjects.getUser() == null || userProjects.getUser().getId() == null) {
            logger.error("Invalid UserProject: User is null or User ID is null");
            throw new InvalidUserProjectException("The Project must be associated with a valid User");
        }

        if (userGateway.findById(userProjects.getUser().getId()) == null) {
            logger.error("User with id {} not found", userProjects.getUser().getId());
            throw new EntityNotFoundException("User with id " + userProjects.getUser().getId() + " not found");
        }

        return userProjectGateway.save(userProjects);
    }
}

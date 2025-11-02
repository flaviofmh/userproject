package com.user.project.User.Project.domain.usecase;

import com.user.project.User.Project.domain.exception.EntityNotFoundException;
import com.user.project.User.Project.domain.exception.InvalidUserProjectException;
import com.user.project.User.Project.domain.model.UserProjects;
import com.user.project.User.Project.domain.repository.UserGateway;
import com.user.project.User.Project.domain.repository.UserProjectGateway;

public class UserProjectCreationUseCase {

    private final UserGateway userGateway;
    private final UserProjectGateway userProjectGateway;

    public UserProjectCreationUseCase(UserProjectGateway userProjectGateway, UserGateway userGateway) {
        this.userGateway = userGateway;
        this.userProjectGateway = userProjectGateway;
    }

    public UserProjects execute(UserProjects userProjects) {
        if (userProjects.getUser() == null || userProjects.getUser().getId() == null) {
            throw new InvalidUserProjectException("The Project must be associated with a valid User");
        }

        if (userGateway.findById(userProjects.getUser().getId()) == null) {
            throw new EntityNotFoundException("User with id " + userProjects.getUser().getId() + " not found");
        }

        return userProjectGateway.save(userProjects);
    }
}

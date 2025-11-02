package com.user.project.User.Project.domain.usecase;

import com.user.project.User.Project.domain.exception.BusinessException;
import com.user.project.User.Project.domain.exception.EntityNotFoundException;
import com.user.project.User.Project.domain.model.User;
import com.user.project.User.Project.domain.repository.UserGateway;

public class RetrieveUserInformationUseCase {

    private final UserGateway userGateway;

    public RetrieveUserInformationUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public User execute(Long id) {
        if (id == null) {
            throw new BusinessException("User id cannot be null");
        }

        var userDomain = userGateway.findById(id);

        if (userDomain == null) {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }

        return userDomain;
    }

}

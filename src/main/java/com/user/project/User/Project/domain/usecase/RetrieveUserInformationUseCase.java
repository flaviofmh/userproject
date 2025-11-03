package com.user.project.User.Project.domain.usecase;

import com.user.project.User.Project.domain.exception.BusinessException;
import com.user.project.User.Project.domain.exception.EntityNotFoundException;
import com.user.project.User.Project.domain.model.User;
import com.user.project.User.Project.domain.repository.UserGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RetrieveUserInformationUseCase {

    private static final Logger logger = LoggerFactory.getLogger(RetrieveUserInformationUseCase.class);
    private final UserGateway userGateway;

    public RetrieveUserInformationUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public User execute(Long id) {
        logger.info("Executing RetrieveUserInformationUseCase with id: {}", id);

        if (id == null) {
            logger.error("User id is null");
            throw new BusinessException("User id cannot be null");
        }

        var userDomain = userGateway.findById(id);

        if (userDomain == null) {
            logger.error("User with id {} not found", id);
            throw new EntityNotFoundException("User with id " + id + " not found");
        }

        logger.info("User with id {} retrieved successfully", id);
        return userDomain;
    }

}

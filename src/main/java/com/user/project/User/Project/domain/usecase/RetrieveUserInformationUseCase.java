package com.user.project.User.Project.domain.usecase;

import com.user.project.User.Project.domain.exception.BusinessException;
import com.user.project.User.Project.domain.exception.EntityNotFoundException;
import com.user.project.User.Project.domain.model.User;
import com.user.project.User.Project.domain.repository.UserRepository;

public class RetrieveUserInformationUseCase {

    private final UserRepository userRepository;

    public RetrieveUserInformationUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(Long id) {
        if (id == null) {
            throw new BusinessException("User id cannot be null");
        }

        var userDomain = userRepository.findById(id);

        if (userDomain == null) {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }

        return userDomain;
    }

}

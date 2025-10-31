package com.user.project.User.Project.domain.usecase;

import com.user.project.User.Project.domain.model.User;
import com.user.project.User.Project.domain.repository.UserRepository;

public class UserCreationUseCase {

    private final UserRepository userRepository;

    public UserCreationUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(User user) {
        return userRepository.save(user);
    }
}

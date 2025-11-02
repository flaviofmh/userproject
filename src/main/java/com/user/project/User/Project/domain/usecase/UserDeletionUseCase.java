package com.user.project.User.Project.domain.usecase;

import com.user.project.User.Project.domain.repository.UserRepository;

public class UserDeletionUseCase {

    private final UserRepository userRepository;

    public UserDeletionUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(Long userId) {
        userRepository.deleteById(userId);
    }

}

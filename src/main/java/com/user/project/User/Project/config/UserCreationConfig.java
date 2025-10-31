package com.user.project.User.Project.config;

import com.user.project.User.Project.domain.repository.UserRepository;
import com.user.project.User.Project.domain.usecase.UserCreationUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserCreationConfig {

    @Bean
    public UserCreationUseCase userCreationUseCase(UserRepository userRepository) {
        return new UserCreationUseCase(userRepository);
    }

}

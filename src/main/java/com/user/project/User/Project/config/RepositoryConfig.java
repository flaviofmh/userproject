package com.user.project.User.Project.config;

import com.user.project.User.Project.domain.repository.UserRepository;
import com.user.project.User.Project.infrastructure.repoimpl.UserEntityRepositoryImpl;
import com.user.project.User.Project.infrastructure.repository.UserEntityRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    public UserRepository userRepository(UserEntityRepository jpaRepository) {
        return new UserEntityRepositoryImpl(jpaRepository);
    }

}

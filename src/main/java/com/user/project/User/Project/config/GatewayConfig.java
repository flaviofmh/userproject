package com.user.project.User.Project.config;

import com.user.project.User.Project.domain.repository.UserGateway;
import com.user.project.User.Project.domain.repository.UserProjectGateway;
import com.user.project.User.Project.infrastructure.gateways.UserGatewayImpl;
import com.user.project.User.Project.infrastructure.gateways.UserProjectGatewayImpl;
import com.user.project.User.Project.infrastructure.repository.UserEntityRepository;
import com.user.project.User.Project.infrastructure.repository.UserProjectEntityRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public UserGateway userRepository(UserEntityRepository jpaRepository) {
        return new UserGatewayImpl(jpaRepository);
    }

    @Bean
    public UserProjectGateway userGatewayImpl(UserProjectEntityRepository userProjectEntityRepository, UserEntityRepository userEntityRepository) {
        return new UserProjectGatewayImpl(userProjectEntityRepository, userEntityRepository);
    }

}

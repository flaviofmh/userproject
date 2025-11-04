package com.user.project.config;

import com.user.project.domain.repository.UserGateway;
import com.user.project.domain.repository.UserProjectGateway;
import com.user.project.infrastructure.gateways.UserGatewayImpl;
import com.user.project.infrastructure.gateways.UserProjectGatewayImpl;
import com.user.project.infrastructure.repository.UserEntityRepository;
import com.user.project.infrastructure.repository.UserProjectEntityRepository;
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

package com.user.project.User.Project.config;

import com.user.project.User.Project.domain.repository.UserGateway;
import com.user.project.User.Project.domain.repository.UserProjectGateway;
import com.user.project.User.Project.domain.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public UserUpsertUseCase userCreationUseCase(UserGateway userGateway) {
        return new UserUpsertUseCase(userGateway);
    }

    @Bean
    public RetrieveUserInformationUseCase retrieveUserInformationUseCase(UserGateway userGateway) {
        return new RetrieveUserInformationUseCase(userGateway);
    }

    @Bean
    public UserDeletionUseCase userDeletionUseCase(UserGateway userGateway) {
        return new UserDeletionUseCase(userGateway);
    }

    @Bean
    public UserProjectCreationUseCase userProjectCreationUseCase(UserProjectGateway userProjectGateway, UserGateway userGateway) {
        return new UserProjectCreationUseCase(userProjectGateway, userGateway);
    }

    @Bean
    public UserProjectRetrievalUseCase userProjectRetrievalUseCase(UserProjectGateway userProjectGateway) {
        return new UserProjectRetrievalUseCase(userProjectGateway);
    }

}

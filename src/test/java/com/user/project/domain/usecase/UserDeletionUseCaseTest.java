package com.user.project.domain.usecase;

import com.user.project.domain.repository.UserGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserDeletionUseCaseTest.TestConfig.class)
public class UserDeletionUseCaseTest {

    @Configuration
    static class TestConfig {
        @Bean
        public UserGateway userGateway() {
            return Mockito.mock(UserGateway.class);
        }

        @Bean
        public UserDeletionUseCase userDeletionUseCase(UserGateway userGateway) {
            return new UserDeletionUseCase(userGateway);
        }
    }

    @Autowired
    private UserDeletionUseCase useCase;

    @Autowired
    private UserGateway userGateway;

    @Test
    void shouldInvokeGatewayDeleteById() {
        Long id = 1L;

        useCase.execute(id);

        verify(userGateway).deleteById(id);
    }

}

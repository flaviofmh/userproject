package com.user.project.User.Project.domain.usecase;

import com.user.project.User.Project.domain.exception.BusinessException;
import com.user.project.User.Project.domain.exception.EntityNotFoundException;
import com.user.project.User.Project.domain.model.User;
import com.user.project.User.Project.domain.repository.UserGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RetrieveUserInformationUseCaseTest.TestConfig.class)
public class RetrieveUserInformationUseCaseTest {

    @Configuration
    static class TestConfig {
        @Bean
        public UserGateway userGateway() {
            return Mockito.mock(UserGateway.class);
        }

        @Bean
        public RetrieveUserInformationUseCase retrieveUserInformationUseCase(UserGateway userGateway) {
            return new RetrieveUserInformationUseCase(userGateway);
        }
    }

    @Autowired
    private RetrieveUserInformationUseCase useCase;

    @Autowired
    private UserGateway userGateway;

    @Test
    void shouldReturnUserWhenExists() {
        Long id = 1L;
        User user = new User(id, "test@mail.com", "password123", "John Doe");
        Mockito.when(userGateway.findById(id)).thenReturn(user);

        User result = useCase.execute(id);

        assertSame(user, result);
        assertEquals(id, result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getPassword(), result.getPassword());

        Mockito.verify(userGateway).findById(id);
    }

    @Test
    void shouldThrowBusinessExceptionWhenIdIsNull() {
        assertThrows(BusinessException.class, () -> useCase.execute(null));
        Mockito.verifyNoInteractions(userGateway);
    }

    @Test
    void shouldThrowEntityNotFoundWhenUserNotFound() {
        Long id = 2L;
        Mockito.when(userGateway.findById(id)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> useCase.execute(id));
        Mockito.verify(userGateway).findById(id);
    }

}

package com.user.project.User.Project.domain.usecase;

import com.user.project.User.Project.domain.model.User;
import com.user.project.User.Project.domain.repository.UserGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserCreationUseCaseTest.TestConfig.class)
public class UserCreationUseCaseTest {

    @Configuration
    static class TestConfig {
        @Bean
        public UserGateway userGateway() {
            return Mockito.mock(UserGateway.class);
        }

        @Bean
        public UserCreationUseCase userCreationUseCase(UserGateway userGateway) {
            return new UserCreationUseCase(userGateway);
        }
    }

    @Autowired
    private UserCreationUseCase useCase;

    @Autowired
    private UserGateway userGateway;

    @Test
    void shouldCreateUserSuccessfully() {
        User user = new User();
        user.setId(1L);
        user.setName("Jane Doe");
        user.setEmail("jane.doe@example.com");

        Mockito.when(userGateway.save(user)).thenReturn(user);

        User result = useCase.execute(user);

        assertNotNull(result, "result should not be null");
        assertEquals(user.getId(), result.getId(), "ids should match");
        assertEquals(user.getName(), result.getName(), "names should match");
        assertEquals(user.getEmail(), result.getEmail(), "emails should match");

        assertNotNull(result.getName());
        assertFalse(result.getName().isBlank(), "name should not be blank");
        assertNotNull(result.getEmail());
        assertFalse(result.getEmail().isBlank(), "email should not be blank");

        Mockito.verify(userGateway).save(user);
    }

}

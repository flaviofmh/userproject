package com.user.project.User.Project.domain.usecase;

import com.user.project.User.Project.domain.exception.EntityNotFoundException;
import com.user.project.User.Project.domain.exception.InvalidUserProjectException;
import com.user.project.User.Project.domain.model.User;
import com.user.project.User.Project.domain.model.UserProjects;
import com.user.project.User.Project.domain.repository.UserGateway;
import com.user.project.User.Project.domain.repository.UserProjectGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserProjectCreationUseCaseTest.TestConfig.class)
public class UserProjectCreationUseCaseTest {

    @Configuration
    static class TestConfig {
        @Bean
        public UserGateway userGateway() {
            return Mockito.mock(UserGateway.class);
        }

        @Bean
        public UserProjectGateway userProjectGateway() {
            return Mockito.mock(UserProjectGateway.class);
        }

        @Bean
        public UserProjectCreationUseCase userProjectCreationUseCase(UserProjectGateway userProjectGateway, UserGateway userGateway) {
            return new UserProjectCreationUseCase(userProjectGateway, userGateway);
        }
    }

    @Autowired
    private UserProjectCreationUseCase useCase;

    @Autowired
    private UserGateway userGateway;

    @Autowired
    private UserProjectGateway userProjectGateway;

    @Test
    void shouldCreateUserProjectWhenUserExists() {
        Long userId = 1L;


        User user = new User();

        user.setId(userId);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        UserProjects userProjects = new UserProjects();
        userProjects.setUser(user);
        userProjects.setName("Integration Project");

        when(userGateway.findById(userId)).thenReturn(user);
        when(userProjectGateway.save(userProjects)).thenReturn(userProjects);

        UserProjects result = useCase.execute(userProjects);

        assertNotNull(result, "result should not be null");
        assertNotNull(result.getUser(), "result.user should not be null");

        assertEquals(userId, result.getUser().getId(), "user id should match");
        assertNotNull(result.getUser().getName(), "user.name should not be null");
        assertNotNull(result.getUser().getEmail(), "user.email should not be null");

        verify(userGateway).findById(userId);
        verify(userProjectGateway).save(userProjects);
    }

    @Test
    void shouldThrowInvalidUserProjectExceptionWhenUserMissing() {
        UserProjects userProjects = Mockito.mock(UserProjects.class);
        when(userProjects.getUser()).thenReturn(null);

        assertThrows(InvalidUserProjectException.class, () -> useCase.execute(userProjects));
        verifyNoInteractions(userProjectGateway);
    }

    @Test
    void shouldThrowEntityNotFoundWhenUserNotFound() {
        Long userId = 2L;
        User user = Mockito.mock(User.class);
        when(user.getId()).thenReturn(userId);

        UserProjects userProjects = Mockito.mock(UserProjects.class);
        when(userProjects.getUser()).thenReturn(user);

        when(userGateway.findById(userId)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> useCase.execute(userProjects));
        verify(userGateway).findById(userId);
        verifyNoInteractions(userProjectGateway);
    }

}

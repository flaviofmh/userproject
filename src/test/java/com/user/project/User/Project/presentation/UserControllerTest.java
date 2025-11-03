package com.user.project.User.Project.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.project.User.Project.domain.exception.BusinessException;
import com.user.project.User.Project.domain.exception.EntityNotFoundException;
import com.user.project.User.Project.domain.model.User;
import com.user.project.User.Project.domain.usecase.RetrieveUserInformationUseCase;
import com.user.project.User.Project.domain.usecase.UserDeletionUseCase;
import com.user.project.User.Project.domain.usecase.UserUpsertUseCase;
import com.user.project.User.Project.presentation.request.UserRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserUpsertUseCase userUpsertUseCase;

    @MockBean
    private RetrieveUserInformationUseCase retrieveUserInformationUseCase;

    @MockBean
    private UserDeletionUseCase userDeletionUseCase;

    @Test
    void shouldCreateUserSuccessfully() throws Exception {
        UserRequest request = new UserRequest("john.doe@example.com", "password123", "John Doe");
        User createdUser = new User(1L, request.email(), request.password(), request.name());

        Mockito.when(userUpsertUseCase.execute(any(User.class))).thenReturn(createdUser);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.password").value("password123"));

        Mockito.verify(userUpsertUseCase).execute(any(User.class));
    }

    @Test
    void shouldThrowValidationExceptionWhenCreatingUser() throws Exception {
        UserRequest request = new UserRequest("invalid-email", "", "");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.type").isNotEmpty())
                .andExpect(jsonPath("$.title").isNotEmpty())
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.userMessage").isNotEmpty())
                .andExpect(jsonPath("$.fields").isNotEmpty());
    }

    @Test
    void shouldGetUserSuccessfully() throws Exception {
        Long userId = 1L;
        User user = new User(userId, "john.doe@example.com", "password123", "John Doe");

        Mockito.when(retrieveUserInformationUseCase.execute(eq(userId))).thenReturn(user);

        mockMvc.perform(get("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.password").value("password123"));

        Mockito.verify(retrieveUserInformationUseCase).execute(eq(userId));
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenGettingUser() throws Exception {
        Long userId = 1L;

        Mockito.when(retrieveUserInformationUseCase.execute(eq(userId)))
                .thenThrow(new EntityNotFoundException("User with id " + userId + " not found"));

        mockMvc.perform(get("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.type").value("https://userproject.com/resource-not-found"))
                .andExpect(jsonPath("$.title").value("Resource not found"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.userMessage").isNotEmpty());
    }

    @Test
    void shouldDeleteUserSuccessfully() throws Exception {
        Long userId = 1L;

        mockMvc.perform(delete("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Mockito.verify(userDeletionUseCase).execute(eq(userId));
    }

    @Test
    void shouldThrowBusinessExceptionWhenDeletingUser() throws Exception {
        Long userId = 1L;

        Mockito.doThrow(new BusinessException("Cannot delete user with id " + userId))
                .when(userDeletionUseCase).execute(eq(userId));

        mockMvc.perform(delete("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.type").isNotEmpty())
                .andExpect(jsonPath("$.title").isNotEmpty())
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.userMessage").isEmpty());
    }

    @Test
    void shouldUpdateUserSuccessfully() throws Exception {
        Long userId = 1L;
        var request = new UserRequest(
                "updated.email@example.com", "updatedPassword123", "Updated Name"
        );
        var existingUser = new User(userId, "old.email@example.com", "oldPassword", "Old Name");
        var updatedUser = new User(userId, request.email(), request.password(), request.name());

        Mockito.when(retrieveUserInformationUseCase.execute(eq(userId))).thenReturn(existingUser);
        Mockito.when(userUpsertUseCase.execute(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.email").value("updated.email@example.com"))
                .andExpect(jsonPath("$.password").value("updatedPassword123"))
                .andExpect(jsonPath("$.name").value("Updated Name"));

        Mockito.verify(retrieveUserInformationUseCase).execute(eq(userId));
        Mockito.verify(userUpsertUseCase).execute(any(User.class));
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenUpdatingUser() throws Exception {
        Long userId = 1L;
        var request = new UserRequest(
                "updated.email@example.com", "updatedPassword123", "Updated Name"
        );

        Mockito.when(retrieveUserInformationUseCase.execute(eq(userId)))
                .thenThrow(new EntityNotFoundException("User with id " + userId + " not found"));

        mockMvc.perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.type").value("https://userproject.com/resource-not-found"))
                .andExpect(jsonPath("$.title").value("Resource not found"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.userMessage").isNotEmpty());

        Mockito.verify(retrieveUserInformationUseCase).execute(eq(userId));
        Mockito.verifyNoInteractions(userUpsertUseCase);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUpdateUserRequests")
    void shouldThrowValidationExceptionWhenUpdatingUser(com.user.project.User.Project.presentation.request.UserRequest request) throws Exception {
        Long userId = 1L;

        mockMvc.perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.type").isNotEmpty())
                .andExpect(jsonPath("$.title").isNotEmpty())
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.userMessage").isNotEmpty())
                .andExpect(jsonPath("$.fields").isNotEmpty());
    }

    static Stream<com.user.project.User.Project.presentation.request.UserRequest> provideInvalidUpdateUserRequests() {
        return Stream.of(
                new UserRequest(null, "validPassword123", "Valid Name"), // Null email
                new UserRequest("valid.email@example.com", null, "Valid Name"), // Null password
                new UserRequest("valid.email@example.com", "validPassword123", "a".repeat(121)), // Name too long
                new UserRequest("a".repeat(201), "validPassword123", "Valid Name") // Email too long
        );
    }
}

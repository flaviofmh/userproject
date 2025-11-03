package com.user.project.User.Project.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.project.User.Project.domain.exception.BusinessException;
import com.user.project.User.Project.domain.exception.EntityNotFoundException;
import com.user.project.User.Project.domain.model.User;
import com.user.project.User.Project.domain.usecase.RetrieveUserInformationUseCase;
import com.user.project.User.Project.domain.usecase.UserCreationUseCase;
import com.user.project.User.Project.domain.usecase.UserDeletionUseCase;
import com.user.project.User.Project.presentation.request.UserRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
    private UserCreationUseCase userCreationUseCase;

    @MockBean
    private RetrieveUserInformationUseCase retrieveUserInformationUseCase;

    @MockBean
    private UserDeletionUseCase userDeletionUseCase;

    @Test
    void shouldCreateUserSuccessfully() throws Exception {
        UserRequest request = new UserRequest("John Doe", "john.doe@example.com", "password123");
        User createdUser = new User(1L, "John Doe", "john.doe@example.com", "password123");

        Mockito.when(userCreationUseCase.execute(any(User.class))).thenReturn(createdUser);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.password").value("password123"));

        Mockito.verify(userCreationUseCase).execute(any(User.class));
    }

    @Test
    void shouldThrowValidationExceptionWhenCreatingUser() throws Exception {
        UserRequest request = new UserRequest("", "invalid-email", ""); // Invalid fields

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
        User user = new User(userId, "John Doe", "john.doe@example.com", "password123");

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
                .andExpect(jsonPath("$.userMessage").isNotEmpty());
    }
}

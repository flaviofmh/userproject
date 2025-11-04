package com.user.project.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.project.domain.exception.EntityNotFoundException;
import com.user.project.domain.exception.InvalidUserProjectException;
import com.user.project.domain.model.User;
import com.user.project.domain.model.UserProjects;
import com.user.project.domain.usecase.UserProjectCreationUseCase;
import com.user.project.domain.usecase.UserProjectRetrievalUseCase;
import com.user.project.presentation.request.ProjectRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserProjectController.class)
public class UserProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserProjectCreationUseCase userProjectCreationUseCase;

    @MockBean
    private UserProjectRetrievalUseCase userProjectRetrievalUseCase;

    @Test
    void shouldCreateProjectSuccessfully() throws Exception {
        Long userId = 1L;
        ProjectRequest request = new ProjectRequest("Test Project");
        UserProjects createdProject = new UserProjects(1L, new User(userId, null, null, null), "Test Project");

        Mockito.when(userProjectCreationUseCase.execute(any(UserProjects.class))).thenReturn(createdProject);

        mockMvc.perform(post("/users/{user-id}/projects", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Project"));

        Mockito.verify(userProjectCreationUseCase).execute(any(UserProjects.class));
    }

    @Test
    void shouldThrowInvalidUserProjectExceptionWhenCreatingProject() throws Exception {
        Long userId = 1L;
        ProjectRequest request = new ProjectRequest("Test Project");

        Mockito.when(userProjectCreationUseCase.execute(any(UserProjects.class)))
                .thenThrow(new InvalidUserProjectException("The Project must be associated with a valid User"));

        mockMvc.perform(post("/users/{user-id}/projects", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.type").isNotEmpty())
                .andExpect(jsonPath("$.title").isNotEmpty())
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.fields").isEmpty());

        Mockito.verify(userProjectCreationUseCase).execute(any(UserProjects.class));
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenCreatingProject() throws Exception {
        Long userId = 1L;
        ProjectRequest request = new ProjectRequest("Test Project");

        Mockito.when(userProjectCreationUseCase.execute(any(UserProjects.class)))
                .thenThrow(new EntityNotFoundException("User with id " + userId + " not found"));

        mockMvc.perform(post("/users/{user-id}/projects", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.type").value("https://userproject.com/resource-not-found"))
                .andExpect(jsonPath("$.title").value("Resource not found"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.userMessage").isNotEmpty())
                .andExpect(jsonPath("$.fields").isEmpty());

        Mockito.verify(userProjectCreationUseCase).execute(any(UserProjects.class));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidProjectNames")
    void shouldThrowValidationExceptionWhenRequestIsInvalid(String name) throws Exception {
        Long userId = 1L;

        ProjectRequest request = new ProjectRequest(name);

        mockMvc.perform(post("/users/{user-id}/projects", userId)
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
    void shouldGetProjectsSuccessfully() throws Exception {
        Long userId = 1L;
        PageRequest pageable = PageRequest.of(0, 10);
        UserProjects project1 = new UserProjects(1L, new User(userId, null, null, null), "Project 1");
        UserProjects project2 = new UserProjects(2L, new User(userId, null, null, null), "Project 2");
        Page<UserProjects> projectPage = new PageImpl<>(List.of(project1, project2), pageable, 2);

        Mockito.when(userProjectRetrievalUseCase.retrieveProjectsByUserId(eq(userId), any(PageRequest.class)))
                .thenReturn(projectPage);

        mockMvc.perform(get("/users/{user-id}/projects", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("Project 1"))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].name").value("Project 2"));

        Mockito.verify(userProjectRetrievalUseCase).retrieveProjectsByUserId(eq(userId), any(PageRequest.class));
    }

    static Stream<String> provideInvalidProjectNames() {
        return Stream.of(null, "a".repeat(121));
    }
}

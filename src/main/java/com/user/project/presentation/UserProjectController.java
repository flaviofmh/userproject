package com.user.project.presentation;

import com.user.project.domain.model.User;
import com.user.project.domain.model.UserProjects;
import com.user.project.domain.usecase.UserProjectCreationUseCase;
import com.user.project.domain.usecase.UserProjectRetrievalUseCase;
import com.user.project.presentation.mapper.UserProjectMapper;
import com.user.project.presentation.request.ProjectRequest;
import com.user.project.presentation.response.ProjectResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users/{user-id}/projects")
public class UserProjectController {

    @Autowired
    private UserProjectCreationUseCase userProjectCreationUseCase;

    @Autowired
    private UserProjectRetrievalUseCase userProjectRetrievalUseCase;

    private final UserProjectMapper userProjectMapper = UserProjectMapper.INSTANCE;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse create(@RequestBody @Valid ProjectRequest projectRequest, @PathVariable("user-id") Long userId) {
        var userProjectDomain = userProjectCreationUseCase.execute(
                new UserProjects(null, new User(userId, null, null, null), projectRequest.name())
        );

        return userProjectMapper.toResponse(userProjectDomain);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ProjectResponse> getProjects(@PathVariable("user-id") Long userId, Pageable pageable) {
        var projectDomainPage = userProjectRetrievalUseCase.retrieveProjectsByUserId(userId, pageable);

        return projectDomainPage.map(userProjectMapper::toResponse);
    }

}

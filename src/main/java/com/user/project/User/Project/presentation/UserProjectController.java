package com.user.project.User.Project.presentation;

import com.user.project.User.Project.domain.model.User;
import com.user.project.User.Project.domain.model.UserProjects;
import com.user.project.User.Project.domain.usecase.UserProjectCreationUseCase;
import com.user.project.User.Project.domain.usecase.UserProjectRetrievalUseCase;
import com.user.project.User.Project.presentation.request.ProjectRequest;
import com.user.project.User.Project.presentation.response.ProjectResponse;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse create(@RequestBody @Valid ProjectRequest projectRequest, @PathVariable("user-id") Long userId) {
        var userProjectDomain = userProjectCreationUseCase.execute(new UserProjects(null, new User(userId, null, null, null), projectRequest.name()));

        return new ProjectResponse(userProjectDomain.getId(), userProjectDomain.getName());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ProjectResponse> getProjects(@PathVariable("user-id") Long userId, Pageable pageable) {
        var projectDomainPage = userProjectRetrievalUseCase.retrieveProjectsByUserId(userId, pageable);

        return projectDomainPage.map(projectDomain ->
                new ProjectResponse(projectDomain.getId(), projectDomain.getName())
        );
    }

}

package com.user.project.User.Project.presentation;

import com.user.project.User.Project.domain.model.User;
import com.user.project.User.Project.domain.model.UserProjects;
import com.user.project.User.Project.domain.usecase.UserProjectCreationUseCase;
import com.user.project.User.Project.presentation.request.ProjectRequest;
import com.user.project.User.Project.presentation.response.ProjectResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{user-id}/projects")
public class UserProjectController {

    @Autowired
    private UserProjectCreationUseCase userProjectCreationUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse create(@RequestBody @Valid ProjectRequest projectRequest, @PathVariable("user-id") Long userId) {
        var userProjectsDomain = userProjectCreationUseCase.execute(new UserProjects(null, new User(userId, null, null, null), projectRequest.name()));

        return new ProjectResponse(userProjectsDomain.getId(), userProjectsDomain.getName());
    }

}

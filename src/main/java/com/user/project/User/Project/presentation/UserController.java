package com.user.project.User.Project.presentation;

import com.user.project.User.Project.domain.model.User;
import com.user.project.User.Project.domain.usecase.RetrieveUserInformationUseCase;
import com.user.project.User.Project.domain.usecase.UserCreationUseCase;
import com.user.project.User.Project.presentation.request.UserRequest;
import com.user.project.User.Project.presentation.response.UserResponse;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserCreationUseCase userCreationUseCase;

    @Autowired
    private RetrieveUserInformationUseCase retrieveUserInformationUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@RequestBody @Valid UserRequest userRequest) {
        final User userDomain = new User();
        BeanUtils.copyProperties(userRequest, userDomain, "id");

        userCreationUseCase.execute(userDomain);

        var userResponse = new UserResponse(
                userDomain.getId(),
                userDomain.getEmail(),
                userDomain.getPassword(),
                userDomain.getName()
        );

        return userResponse;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUser(@PathVariable("id") Long id) {
        var userDomain = retrieveUserInformationUseCase.execute(id);

        return new UserResponse(
                userDomain.getId(),
                userDomain.getEmail(),
                userDomain.getPassword(),
                userDomain.getName()
        );
    }

}

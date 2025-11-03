package com.user.project.User.Project.presentation;

import com.user.project.User.Project.domain.model.User;
import com.user.project.User.Project.domain.usecase.RetrieveUserInformationUseCase;
import com.user.project.User.Project.domain.usecase.UserUpsertUseCase;
import com.user.project.User.Project.domain.usecase.UserDeletionUseCase;
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
    private UserUpsertUseCase userUpsertUseCase;

    @Autowired
    private RetrieveUserInformationUseCase retrieveUserInformationUseCase;

    @Autowired
    private UserDeletionUseCase userDeletionUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@RequestBody @Valid UserRequest userRequest) {
        final User userDomain = new User();
        BeanUtils.copyProperties(userRequest, userDomain, "id");

        userUpsertUseCase.execute(userDomain);

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

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") Long id) {
        userDeletionUseCase.execute(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse updateUser(@PathVariable("id") Long id, @RequestBody @Valid UserRequest userRequest) {
        final User userDomain = new User();
        retrieveUserInformationUseCase.execute(id);

        BeanUtils.copyProperties(userRequest, userDomain, "id");
        userDomain.setId(id);
        userUpsertUseCase.execute(userDomain);
        var userResponse = new UserResponse(
                userDomain.getId(),
                userDomain.getEmail(),
                userDomain.getPassword(),
                userDomain.getName()
        );
        return userResponse;
    }

}

package com.user.project.User.Project.presentation;

import com.user.project.User.Project.domain.model.User;
import com.user.project.User.Project.domain.usecase.RetrieveUserInformationUseCase;
import com.user.project.User.Project.domain.usecase.UserDeletionUseCase;
import com.user.project.User.Project.domain.usecase.UserUpsertUseCase;
import com.user.project.User.Project.presentation.mapper.UserMapper;
import com.user.project.User.Project.presentation.request.UserRequest;
import com.user.project.User.Project.presentation.response.UserResponse;
import jakarta.validation.Valid;
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

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@RequestBody @Valid UserRequest userRequest) {
        User userDomain = userMapper.toDomain(userRequest);
        User createdUser = userUpsertUseCase.execute(userDomain);
        return userMapper.toResponse(createdUser);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUser(@PathVariable("id") Long id) {
        User userDomain = retrieveUserInformationUseCase.execute(id);
        return userMapper.toResponse(userDomain);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") Long id) {
        userDeletionUseCase.execute(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse updateUser(@PathVariable("id") Long id, @RequestBody @Valid UserRequest userRequest) {
        retrieveUserInformationUseCase.execute(id);
        User userDomain = userMapper.toDomain(userRequest);
        userDomain.setId(id);
        User updatedUser = userUpsertUseCase.execute(userDomain);
        return userMapper.toResponse(updatedUser);
    }
}

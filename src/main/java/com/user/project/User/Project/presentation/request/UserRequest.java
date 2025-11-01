package com.user.project.User.Project.presentation.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequest(@NotNull @Size(max = 200) String email, @NotNull @Size(max = 129) String password, @Size(max = 120) String name) { }

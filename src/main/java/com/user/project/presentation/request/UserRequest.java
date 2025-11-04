package com.user.project.presentation.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequest(@NotNull @Size(max = 200) @Email String email, @NotNull @Size(max = 129) String password, @Size(max = 120) String name) { }

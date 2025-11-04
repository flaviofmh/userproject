package com.user.project.presentation.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProjectRequest(@NotNull @Size(max = 120) String name) { }

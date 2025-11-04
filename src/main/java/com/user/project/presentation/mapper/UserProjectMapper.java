package com.user.project.presentation.mapper;

import com.user.project.domain.model.UserProjects;
import com.user.project.presentation.response.ProjectResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserProjectMapper {

    UserProjectMapper INSTANCE = Mappers.getMapper(UserProjectMapper.class);

    ProjectResponse toResponse(UserProjects userProjects);
}

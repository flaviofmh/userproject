package com.user.project.User.Project.presentation.mapper;

import com.user.project.User.Project.domain.model.UserProjects;
import com.user.project.User.Project.presentation.response.ProjectResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserProjectMapper {

    UserProjectMapper INSTANCE = Mappers.getMapper(UserProjectMapper.class);

    ProjectResponse toResponse(UserProjects userProjects);
}

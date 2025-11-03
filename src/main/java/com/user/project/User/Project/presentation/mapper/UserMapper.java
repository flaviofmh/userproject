package com.user.project.User.Project.presentation.mapper;

import com.user.project.User.Project.domain.model.User;
import com.user.project.User.Project.presentation.request.UserRequest;
import com.user.project.User.Project.presentation.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    User toDomain(UserRequest userRequest);

    UserResponse toResponse(User user);

    UserRequest toRequest(User user);
}

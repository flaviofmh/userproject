package com.user.project.presentation.mapper;

import com.user.project.domain.model.User;
import com.user.project.presentation.request.UserRequest;
import com.user.project.presentation.response.UserResponse;
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

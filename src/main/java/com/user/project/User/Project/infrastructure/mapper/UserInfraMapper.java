package com.user.project.User.Project.infrastructure.mapper;

import com.user.project.User.Project.domain.model.User;
import com.user.project.User.Project.infrastructure.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserInfraMapper {

    UserInfraMapper INSTANCE = Mappers.getMapper(UserInfraMapper.class);

    User toDomain(UserEntity userEntity);

    UserEntity toEntity(User user);
}

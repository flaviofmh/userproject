package com.user.project.User.Project.infrastructure.mapper;

import com.user.project.User.Project.domain.model.UserProjects;
import com.user.project.User.Project.infrastructure.entity.UserEntity;
import com.user.project.User.Project.infrastructure.entity.UserProjectEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = UserProjectInfraMapper.class)
public interface UserProjectInfraMapper {

    UserProjectInfraMapper INSTANCE = Mappers.getMapper(UserProjectInfraMapper.class);

    @Mapping(source = "userEntity", target = "user")
    @Mapping(source = "userProjectEntity.id", target = "id")
    @Mapping(source = "userProjectEntity.name", target = "name")
    UserProjects toDomain(UserProjectEntity userProjectEntity, UserEntity userEntity);

    UserProjectEntity toEntity(UserProjects userProjects);
}

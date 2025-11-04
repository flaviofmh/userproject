package com.user.project.infrastructure.gateways;

import com.user.project.domain.model.UserProjects;
import com.user.project.domain.repository.UserProjectGateway;
import com.user.project.infrastructure.entity.UserProjectEntity;
import com.user.project.infrastructure.mapper.UserProjectInfraMapper;
import com.user.project.infrastructure.repository.UserEntityRepository;
import com.user.project.infrastructure.repository.UserProjectEntityRepository;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class UserProjectGatewayImpl implements UserProjectGateway {

    private final UserProjectInfraMapper userProjectInfraMapper = UserProjectInfraMapper.INSTANCE;

    private final UserEntityRepository userEntityRepository;
    private final UserProjectEntityRepository userProjectEntityRepository;

    public UserProjectGatewayImpl(UserProjectEntityRepository userProjectEntityRepository, UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
        this.userProjectEntityRepository = userProjectEntityRepository;
    }

    @Override
    public UserProjects save(UserProjects userProjects) {
        var userEntity = userEntityRepository.findById(userProjects.getUser().getId())
                .orElseThrow(() -> new InternalException("Data inconsistency: User with id " + userProjects.getUser().getId() + " not found"));

        var userProjectEntity = new UserProjectEntity(null, userEntity, userProjects.getName());
        userProjectEntityRepository.save(userProjectEntity);

        var userProjectsSaved = userProjectInfraMapper.toDomain(userProjectEntity, userEntity);

        return userProjectsSaved;
    }

    @Override
    public Page<UserProjects> findAllByUserId(Long id, Pageable pageable) {
        Page<UserProjectEntity> entities = userProjectEntityRepository.findAllByUserId(id, pageable);
        return entities.map(entity ->
                userProjectInfraMapper.toDomain(entity, null)
        );
    }
}

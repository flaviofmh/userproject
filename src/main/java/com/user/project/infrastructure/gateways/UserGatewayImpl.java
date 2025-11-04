package com.user.project.infrastructure.gateways;

import com.user.project.domain.exception.EntityNotFoundException;
import com.user.project.domain.model.User;
import com.user.project.domain.repository.UserGateway;
import com.user.project.infrastructure.mapper.UserInfraMapper;
import com.user.project.infrastructure.repository.UserEntityRepository;

public class UserGatewayImpl implements UserGateway {

    private final UserEntityRepository userEntityRepository;

    private final UserInfraMapper userInfraMapper = UserInfraMapper.INSTANCE;

    public UserGatewayImpl(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    @Override
    public User save(User user) {
        var userEntity = userInfraMapper.toEntity(user);

        userEntityRepository.save(userEntity);

        user.setId(userEntity.getId());
        return user;
    }

    @Override
    public User findById(Long id) {
        return userEntityRepository.findById(id)
                .map(userEntity -> userInfraMapper.toDomain(userEntity))
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public void deleteById(Long userId) {
        userEntityRepository.deleteById(userId);
    }


}

package com.user.project.User.Project.infrastructure.repoimpl;

import com.user.project.User.Project.domain.exception.EntityNotFoundException;
import com.user.project.User.Project.domain.model.User;
import com.user.project.User.Project.domain.repository.UserRepository;
import com.user.project.User.Project.infrastructure.entity.UserEntity;
import com.user.project.User.Project.infrastructure.repository.UserEntityRepository;

public class UserEntityRepositoryImpl implements UserRepository {

    private final UserEntityRepository userEntityRepository;

    public UserEntityRepositoryImpl(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    @Override
    public User save(User user) {
        var userEntity = new UserEntity();
        userEntity.setEmail(user.getEmail());
        userEntity.setName(user.getName());
        userEntity.setPassword(user.getPassword());
        userEntityRepository.save(userEntity);

        user.setId(userEntity.getId());
        return user;
    }

    @Override
    public User findById(Long id) {
        return userEntityRepository.findById(id)
                .map(userEntity -> new User(userEntity.getId(), userEntity.getName(), userEntity.getEmail(), userEntity.getPassword()))
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }


}

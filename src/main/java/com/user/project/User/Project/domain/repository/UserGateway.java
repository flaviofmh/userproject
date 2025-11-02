package com.user.project.User.Project.domain.repository;

import com.user.project.User.Project.domain.model.User;

public interface UserGateway {

    User save(User user);
    User findById(Long id);
    void deleteById(Long userId);
}

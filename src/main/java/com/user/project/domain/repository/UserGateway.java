package com.user.project.domain.repository;

import com.user.project.domain.model.User;

public interface UserGateway {

    User save(User user);
    User findById(Long id);
    void deleteById(Long userId);
}

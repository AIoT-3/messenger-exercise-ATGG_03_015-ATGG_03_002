package com.nhnacademy.messenger.server.user.repository;

import com.nhnacademy.messenger.server.user.domain.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findById(String userId);

    boolean existsById(String userId);

    void deleteById(String userId);
}

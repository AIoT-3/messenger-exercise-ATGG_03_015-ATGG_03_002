package com.nhnacademy.messenger.server.user.service;

import com.nhnacademy.messenger.server.user.domain.User;

import java.util.Optional;

public interface UserService {
    User registerUser(User user);

    User getUserById(String userId);

    Optional<User> findById(String userId);

    User doLogin(String userId, String password);

    java.util.List<User> getAllUsers();
}

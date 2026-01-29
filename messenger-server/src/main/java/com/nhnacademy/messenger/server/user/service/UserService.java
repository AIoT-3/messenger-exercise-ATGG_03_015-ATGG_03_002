package com.nhnacademy.messenger.server.user.service;

import com.nhnacademy.messenger.server.user.domain.User;

public interface UserService {
    User registerUser(User user);

    User getUserById(String userId);

    User login(String userId, String password);
}

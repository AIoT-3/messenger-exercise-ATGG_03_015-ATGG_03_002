package com.nhnacademy.messenger.server.user.repository.impl;

import com.nhnacademy.messenger.server.user.domain.User;
import com.nhnacademy.messenger.server.user.repository.UserRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository implements UserRepository {

    private final Map<String, User> userMap;

    public InMemoryUserRepository() {
        this.userMap = new ConcurrentHashMap<>();
        userMap.put("marco", new User("marco", "마르코", "nhnacademy123"));
    }

    @Override
    public User save(User user) {
        userMap.put(user.getUserId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(String userId) {
        return Optional.ofNullable(userMap.get(userId));
    }

    @Override
    public boolean existsById(String userId) {
        return userMap.containsKey(userId);
    }

    @Override
    public void deleteById(String userId) {
        userMap.remove(userId);
    }
}

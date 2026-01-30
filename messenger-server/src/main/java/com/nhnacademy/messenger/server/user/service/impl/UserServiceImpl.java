package com.nhnacademy.messenger.server.user.service.impl;

import com.nhnacademy.messenger.common.exception.MessengerException;
import com.nhnacademy.messenger.server.user.domain.User;
import com.nhnacademy.messenger.server.user.repository.UserRepository;
import com.nhnacademy.messenger.server.user.service.UserService;
import lombok.AllArgsConstructor;


import static com.nhnacademy.messenger.common.message.data.error.ErrorCode.*;

@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User registerUser(User user) {
        if (userRepository.existsById(user.getUserId())) {
            throw new MessengerException(USER_ALREADY_EXISTS, "이미 존재하는 사용자 ID입니다: " + user.getUserId());
        }
        return userRepository.save(user);
    }

    @Override
    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new MessengerException(USER_NOT_FOUND, "사용자를 찾을 수 없습니다: " + userId));
    }

    @Override
    public User doLogin(String userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new MessengerException(AUTH_INVALID_CREDENTIALS, "존재하지 않는 사용자이거나 잘못된 비밀번호입니다."));
        //TODO: 나중에 암호화된 비밀번호 비교로 변경 필요
        if (!user.getPassword().equals(password)) {
            throw new MessengerException(AUTH_INVALID_CREDENTIALS, "존재하지 않는 사용자이거나 잘못된 비밀번호입니다.");
        }
        return user;
    }
}

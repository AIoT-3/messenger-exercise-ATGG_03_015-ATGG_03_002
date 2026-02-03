package com.nhnacademy.messenger.client.domain.user.controller;

import com.nhnacademy.messenger.client.domain.user.service.UserClientService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserController {

    private final UserClientService userClientService;

    public void login(String userId, String password) {
        userClientService.login(userId, password);
    }

    public void logout() {
        userClientService.logout();
    }
}

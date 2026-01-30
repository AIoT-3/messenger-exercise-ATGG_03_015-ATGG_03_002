package com.nhnacademy.messenger.server.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User {
    private String userId;
    private String userName;
    private String password;
}

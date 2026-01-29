package com.nhnacademy.messenger.server.session.domain;

import com.nhnacademy.messenger.common.util.reader.bio.StreamMessageReader;
import com.nhnacademy.messenger.server.session.manager.SessionManager;
import com.nhnacademy.messenger.server.user.domain.User;
import com.nhnacademy.messenger.server.user.service.UserService;
import lombok.Getter;

import java.io.DataOutputStream;
import java.net.Socket;

public class Session implements Runnable {

    @Getter
    private final String id;
    private final Socket socket;
    private final UserService userService;
    private final SessionManager sessionManager;

    @Getter
    private User user; // 로그인 전에는 null

    private StreamMessageReader reader;
    private DataOutputStream out;

    public Session(Socket socket, UserService userService, SessionManager sessionManager) {
        this.id = java.util.UUID.randomUUID().toString();
        this.socket = socket;
        this.userService = userService;
        this.sessionManager = sessionManager;
    }

    @Override
    public void run() {

    }
}

package com.nhnacademy.messenger.server.runnable;

import com.nhnacademy.messenger.server.session.domain.Session;
import com.nhnacademy.messenger.server.session.manager.SessionManager;
import com.nhnacademy.messenger.server.user.repository.impl.InMemoryUserRepository;
import com.nhnacademy.messenger.server.user.service.UserService;
import com.nhnacademy.messenger.server.user.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static com.nhnacademy.messenger.common.config.AppConstant.DEFAULT_SERVER_PORT;

@Slf4j
public class MessageServer implements Runnable {

    private final ServerSocket serverSocket;
    private final SessionManager sessionManager;
    private final UserService userService;

    public MessageServer() {
        this(DEFAULT_SERVER_PORT);
    }

    public MessageServer(int port) {
        if (port <= 0) {
            throw new IllegalArgumentException(String.format("port:%d", port));
        }

        this.sessionManager = new SessionManager();
        this.userService = new UserServiceImpl(new InMemoryUserRepository());

        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Socket socket = serverSocket.accept();
                try {
                    Session session = new Session(socket, sessionManager, userService);
                    Thread.ofVirtual().start(session);
                } catch (Exception e) {
                    log.error("세션 초기화 중 오류 발생: {}", e.getMessage());
                }
            } catch (IOException e) {
                log.error("서버 소켓 오류: {}", e.getMessage());
                break;
            }
        }
    }
}

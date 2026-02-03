package com.nhnacademy.messenger.server.network;

import com.nhnacademy.messenger.common.event.EventBus;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.server.room.handler.CreateRoomRequestHandler;
import com.nhnacademy.messenger.server.room.handler.EnterRoomRequestHandler;
import com.nhnacademy.messenger.server.room.handler.ExitRoomRequestHandler;
import com.nhnacademy.messenger.server.room.handler.ListRoomRequestHandler;
import com.nhnacademy.messenger.server.room.repository.impl.InMemoryChatRoomRepository;
import com.nhnacademy.messenger.server.room.service.ChatRoomService;
import com.nhnacademy.messenger.server.room.service.impl.ChatRoomServiceImpl;
import com.nhnacademy.messenger.server.session.domain.Session;
import com.nhnacademy.messenger.server.session.manager.SessionManager;
import com.nhnacademy.messenger.server.user.handler.LoginRequestHandler;
import com.nhnacademy.messenger.server.user.handler.LogoutRequestHandler;
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
    private final ChatRoomService chatRoomService;
    private final MessageDispatcher messageDispatcher;

    public MessageServer() {
        this(DEFAULT_SERVER_PORT);
    }

    public MessageServer(int port) {
        if (port <= 0) {
            throw new IllegalArgumentException(String.format("port:%d", port));
        }

        // 1. 서비스 초기화
        this.sessionManager = new SessionManager();
        this.userService = new UserServiceImpl(new InMemoryUserRepository());
        this.chatRoomService = new ChatRoomServiceImpl(new InMemoryChatRoomRepository());
        
        EventBus.INSTANCE.register(this.chatRoomService);

        // 2. 디스패처 및 핸들러 초기화
        this.messageDispatcher = new MessageDispatcher();
        this.messageDispatcher.register(MessageType.LOGIN, new LoginRequestHandler(userService, sessionManager));
        this.messageDispatcher.register(MessageType.LOGOUT, new LogoutRequestHandler());
        this.messageDispatcher.register(MessageType.CHAT_ROOM_CREATE, new CreateRoomRequestHandler(chatRoomService));
        this.messageDispatcher.register(MessageType.CHAT_ROOM_LIST, new ListRoomRequestHandler(chatRoomService));
        this.messageDispatcher.register(MessageType.CHAT_ROOM_ENTER, new EnterRoomRequestHandler(chatRoomService));
        this.messageDispatcher.register(MessageType.CHAT_ROOM_EXIT, new ExitRoomRequestHandler(chatRoomService));

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
                    Session session = new Session(
                            socket, messageDispatcher, sessionManager); // userService 제거됨
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

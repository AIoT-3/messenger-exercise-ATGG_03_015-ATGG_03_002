package com.nhnacademy.messenger.server.network;

import com.nhnacademy.messenger.common.event.EventBus;
import com.nhnacademy.messenger.common.message.header.MessageType;
import com.nhnacademy.messenger.server.chat.handler.ChatHistoryRequestHandler;
import com.nhnacademy.messenger.server.chat.handler.ChatRequestHandler;
import com.nhnacademy.messenger.server.chat.handler.PrivateChatRequestHandler;
import com.nhnacademy.messenger.server.chat.repository.ChatRepository;
import com.nhnacademy.messenger.server.chat.repository.impl.InMemoryChatRepository;
import com.nhnacademy.messenger.server.chat.service.ChatService;
import com.nhnacademy.messenger.server.chat.service.impl.ChatServiceImpl;
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
import com.nhnacademy.messenger.server.user.handler.UserListRequestHandler;
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
    private final MessageDispatcher messageDispatcher;

    private final UserService userService;
    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

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
        
        ChatRepository chatRepository = new InMemoryChatRepository();
        this.chatService = new ChatServiceImpl(chatRepository);
        
        EventBus.INSTANCE.register(this.chatRoomService);

        // 2. 디스패처 및 핸들러 초기화
        this.messageDispatcher = new MessageDispatcher();
        this.messageDispatcher.register(MessageType.LOGIN, new LoginRequestHandler(userService, sessionManager));
        this.messageDispatcher.register(MessageType.LOGOUT, new LogoutRequestHandler());
        this.messageDispatcher.register(MessageType.USER_LIST, new UserListRequestHandler(userService, sessionManager));
        this.messageDispatcher.register(MessageType.CHAT_ROOM_CREATE, new CreateRoomRequestHandler(chatRoomService));
        this.messageDispatcher.register(MessageType.CHAT_ROOM_LIST, new ListRoomRequestHandler(chatRoomService));
        this.messageDispatcher.register(MessageType.CHAT_ROOM_ENTER, new EnterRoomRequestHandler(chatRoomService));
        this.messageDispatcher.register(MessageType.CHAT_ROOM_EXIT, new ExitRoomRequestHandler(chatRoomService));
        this.messageDispatcher.register(MessageType.CHAT_MESSAGE, new ChatRequestHandler(chatService, chatRoomService));
        this.messageDispatcher.register(MessageType.PRIVATE_MESSAGE, new PrivateChatRequestHandler(sessionManager));
        this.messageDispatcher.register(MessageType.CHAT_MESSAGE_HISTORY, new ChatHistoryRequestHandler(userService, chatService, chatRoomService));

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
                log.info("새로운 클라이언트 연결됨: {}", socket.getRemoteSocketAddress());
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

package com.nhnacademy.messenger.server.network;

import com.nhnacademy.messenger.common.event.EventBus;
import com.nhnacademy.messenger.server.chat.handler.*;
import com.nhnacademy.messenger.server.chat.repository.impl.InMemoryChatRepository;
import com.nhnacademy.messenger.server.chat.service.ChatService;
import com.nhnacademy.messenger.server.chat.service.impl.ChatServiceImpl;
import com.nhnacademy.messenger.server.queue.BlockingMessageQueue;
import com.nhnacademy.messenger.server.queue.MessageWorker;
import com.nhnacademy.messenger.server.room.handler.*;
import com.nhnacademy.messenger.server.room.repository.impl.InMemoryChatRoomRepository;
import com.nhnacademy.messenger.server.room.service.ChatRoomService;
import com.nhnacademy.messenger.server.room.service.impl.ChatRoomServiceImpl;
import com.nhnacademy.messenger.server.session.domain.NioSession;
import com.nhnacademy.messenger.server.session.manager.SessionManager;
import com.nhnacademy.messenger.server.user.handler.*;
import com.nhnacademy.messenger.server.user.repository.impl.InMemoryUserRepository;
import com.nhnacademy.messenger.server.user.service.UserService;
import com.nhnacademy.messenger.server.user.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import static com.nhnacademy.messenger.common.config.AppConstant.DEFAULT_SERVER_PORT;
import static com.nhnacademy.messenger.common.message.header.MessageType.*;

/**
 * NioMessageServer
 * 역할: Boss Selector를 통한 연결 수락 및 다중 Worker Selector 배분
 */
@Slf4j
public class NioMessageServer implements Runnable {

    private final int port;
    private final SessionManager sessionManager;
    private final MessageDispatcher messageDispatcher;
    
    private final BlockingMessageQueue messageQueue;
    private final MessageWorker messageWorker;
    private final int workerCount = Runtime.getRuntime().availableProcessors();
    private final NioWorker[] workers = new NioWorker[workerCount];
    private final AtomicInteger roundRobin = new AtomicInteger(0);

    public NioMessageServer() {
        this(DEFAULT_SERVER_PORT);
    }

    public NioMessageServer(int port) {
        this.port = port;

        // 1. 인프라 초기화
        this.sessionManager = new SessionManager();
        this.messageQueue = new BlockingMessageQueue();
        
        // 2. 서비스 및 디스패처 초기화
        UserService userService = new UserServiceImpl(new InMemoryUserRepository());
        ChatRoomService chatRoomService = new ChatRoomServiceImpl(new InMemoryChatRoomRepository());
        ChatService chatService = new ChatServiceImpl(new InMemoryChatRepository());
        EventBus.INSTANCE.register(chatRoomService);

        this.messageDispatcher = new MessageDispatcher();
        registerHandlers(userService, chatRoomService, chatService);

        // 3. 비즈니스 로직 소비자(MessageWorker) 초기화
        this.messageWorker = new MessageWorker(messageQueue, messageDispatcher);
    }

    private void registerHandlers(UserService userService, ChatRoomService chatRoomService, ChatService chatService) {
        messageDispatcher.register(LOGIN, new LoginRequestHandler(userService, sessionManager));
        messageDispatcher.register(LOGOUT, new LogoutRequestHandler());
        messageDispatcher.register(USER_LIST, new UserListRequestHandler(userService, sessionManager));
        messageDispatcher.register(CHAT_ROOM_CREATE, new CreateRoomRequestHandler(chatRoomService));
        messageDispatcher.register(CHAT_ROOM_LIST, new ListRoomRequestHandler(chatRoomService));
        messageDispatcher.register(CHAT_ROOM_ENTER, new EnterRoomRequestHandler(chatRoomService));
        messageDispatcher.register(CHAT_ROOM_EXIT, new ExitRoomRequestHandler(chatRoomService));
        messageDispatcher.register(CHAT_MESSAGE, new ChatRequestHandler(chatService, chatRoomService));
        messageDispatcher.register(PRIVATE_MESSAGE, new PrivateChatRequestHandler(sessionManager));
        messageDispatcher.register(CHAT_MESSAGE_HISTORY, new ChatHistoryRequestHandler(userService, chatService, chatRoomService));
        messageDispatcher.register(FILE_TRANSFER, new FileTransferRequestHandler(chatService, chatRoomService));
    }

    @Override
    public void run() {
        log.info("NIO Message Server 가동 (Port: {}, I/O Workers: {})", port, workerCount);

        // 1. 비즈니스 워커 기동
        new Thread(messageWorker, "Biz-Worker").start();

        try {
            // 2. I/O 워커(NioWorker) 기동
            for (int i = 0; i < workerCount; i++) {
                workers[i] = new NioWorker(i, messageQueue);
                new Thread(workers[i], "IO-Worker-" + i).start();
            }

            // 3. Boss Selector (Accept) 기동
            try (Selector bossSelector = Selector.open();
                 ServerSocketChannel serverSocket = ServerSocketChannel.open()) {

                serverSocket.bind(new InetSocketAddress(port));
                serverSocket.configureBlocking(false);
                serverSocket.register(bossSelector, SelectionKey.OP_ACCEPT);

                while (!Thread.currentThread().isInterrupted()) {
                    bossSelector.select();
                    Iterator<SelectionKey> keys = bossSelector.selectedKeys().iterator();
                    while (keys.hasNext()) {
                        SelectionKey key = keys.next();
                        keys.remove();
                        if (key.isAcceptable()) {
                            accept(serverSocket);
                        }
                    }
                }
            }
        } catch (IOException e) {
            log.error("NIO 서버 엔진 치명적 오류", e);
        }
    }

    private void accept(ServerSocketChannel serverSocket) throws IOException {
        SocketChannel clientChannel = serverSocket.accept();
        clientChannel.configureBlocking(false);
        log.info("NIO 연결 수락: {}", clientChannel.getRemoteAddress());

        // NioSession 생성 및 Worker에 할당
        NioSession session = new NioSession(clientChannel, messageDispatcher, sessionManager);
        NioWorker targetWorker = workers[roundRobin.getAndIncrement() % workerCount];
        targetWorker.register(session);
    }
}
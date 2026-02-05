package com.nhnacademy.messenger.client;

import com.nhnacademy.messenger.client.domain.chat.handler.ChatHistoryResponseHandler;
import com.nhnacademy.messenger.client.domain.chat.handler.ChatResponseHandler;
import com.nhnacademy.messenger.client.domain.chat.handler.PushMessageHandler;
import com.nhnacademy.messenger.client.domain.error.handler.ErrorResponseHandler;
import com.nhnacademy.messenger.client.domain.room.handler.*;
import com.nhnacademy.messenger.client.domain.room.service.ChatRoomClientService;
import com.nhnacademy.messenger.client.domain.user.handler.LoginResponseHandler;
import com.nhnacademy.messenger.client.domain.user.handler.LogoutResponseHandler;
import com.nhnacademy.messenger.client.domain.user.handler.UserListResponseHandler;
import com.nhnacademy.messenger.client.domain.user.service.UserClientService;
import com.nhnacademy.messenger.client.network.ClientMessageDispatcher;
import com.nhnacademy.messenger.client.network.MessageClient;
import com.nhnacademy.messenger.client.ui.ClientUiEventListener;
import com.nhnacademy.messenger.client.ui.gui.GuiView;
import com.nhnacademy.messenger.client.ui.gui.manager.PrivateChatManager;
import com.nhnacademy.messenger.client.ui.gui.manager.RoomChatManager;
import com.nhnacademy.messenger.client.ui.gui.panel.LoginPanel;
import com.nhnacademy.messenger.client.ui.gui.panel.RoomListPanel;
import com.nhnacademy.messenger.common.event.EventBus;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;

import static com.nhnacademy.messenger.common.config.AppConstant.*;
import static com.nhnacademy.messenger.common.message.header.MessageType.*;

@Slf4j
public class GuiMain {

    public static void main(String[] args) {
        // 1. 네트워크 및 서비스 초기화
        ClientMessageDispatcher networkDispatcher = new ClientMessageDispatcher();
        MessageClient client = new MessageClient(DEFAULT_SERVER_ADDRESS, DEFAULT_SERVER_PORT, networkDispatcher);
        UserClientService userClientService = new UserClientService(client);
        ChatRoomClientService chatRoomClientService = new ChatRoomClientService(client);

        // 2. 네트워크 핸들러 등록
        networkDispatcher.register(LOGIN_SUCCESS, new LoginResponseHandler());
        networkDispatcher.register(LOGOUT_SUCCESS, new LogoutResponseHandler());
        networkDispatcher.register(USER_LIST_SUCCESS, new UserListResponseHandler());
        networkDispatcher.register(CHAT_ROOM_CREATE_SUCCESS, new CreateRoomResponseHandler());
        networkDispatcher.register(CHAT_ROOM_LIST_SUCCESS, new ListRoomResponseHandler());
        networkDispatcher.register(CHAT_ROOM_ENTER_SUCCESS, new EnterRoomResponseHandler(chatRoomClientService));
        networkDispatcher.register(CHAT_ROOM_EXIT_SUCCESS, new ExitRoomResponseHandler());
        networkDispatcher.register(CHAT_MESSAGE_SUCCESS, new ChatResponseHandler());
        networkDispatcher.register(CHAT_MESSAGE_HISTORY_SUCCESS, new ChatHistoryResponseHandler());
        networkDispatcher.register(PUSH_NEW_MESSAGE, new PushMessageHandler());
        networkDispatcher.register(PUSH_ROOM_ENTER, new PushRoomEnterHandler());
        networkDispatcher.register(PUSH_ROOM_EXIT, new PushRoomExitHandler());
        networkDispatcher.register(ERROR, new ErrorResponseHandler());

        RoomChatManager roomChatManager = new RoomChatManager(chatRoomClientService);
        PrivateChatManager privateChatManager = new PrivateChatManager(chatRoomClientService);
        // 3. GUI 초기화
        LoginPanel loginPanel = new LoginPanel(userClientService);
        RoomListPanel roomListPanel = new RoomListPanel(userClientService, chatRoomClientService, privateChatManager);

        GuiView view = new GuiView(loginPanel, roomListPanel, roomChatManager, privateChatManager);

        // 4. UI 리스너 등록
        ClientUiEventListener uiListener = new ClientUiEventListener(view);
        EventBus.INSTANCE.register(uiListener);

        try {
            // 5. 서버 연결 및 앱 시작
            client.connect();
            view.start();
            log.info("GUI 메신저 클라이언트를 시작합니다.");

        } catch (Exception e) {
            log.error("클라이언트 실행 중 오류 발생", e);
            JOptionPane.showMessageDialog(null, "서버 연결 실패: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}
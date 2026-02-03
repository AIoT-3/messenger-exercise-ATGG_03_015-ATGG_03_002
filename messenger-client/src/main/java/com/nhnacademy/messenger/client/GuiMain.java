package com.nhnacademy.messenger.client;

import com.nhnacademy.messenger.client.domain.chat.handler.ChatResponseHandler;
import com.nhnacademy.messenger.client.domain.chat.listener.PushMessageListener;
import com.nhnacademy.messenger.client.domain.error.handler.ErrorResponseHandler;
import com.nhnacademy.messenger.client.domain.room.handler.CreateRoomResponseHandler;
import com.nhnacademy.messenger.client.domain.room.handler.EnterRoomResponseHandler;
import com.nhnacademy.messenger.client.domain.room.handler.ListRoomResponseHandler;
import com.nhnacademy.messenger.client.domain.room.service.ChatRoomClientService;
import com.nhnacademy.messenger.client.domain.user.handler.LoginResponseHandler;
import com.nhnacademy.messenger.client.domain.user.handler.LogoutResponseHandler;
import com.nhnacademy.messenger.client.domain.user.service.UserClientService;
import com.nhnacademy.messenger.client.network.ClientMessageDispatcher;
import com.nhnacademy.messenger.client.network.MessageClient;
import com.nhnacademy.messenger.client.ui.ClientUiEventListener;
import com.nhnacademy.messenger.client.ui.gui.GuiView;
import com.nhnacademy.messenger.client.ui.gui.panel.LoginPanel;
import com.nhnacademy.messenger.client.ui.gui.panel.RoomChatPanel;
import com.nhnacademy.messenger.client.ui.gui.panel.RoomListPanel;
import com.nhnacademy.messenger.common.event.EventBus;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;

import static com.nhnacademy.messenger.common.config.AppConstant.*;
import static com.nhnacademy.messenger.common.message.header.MessageType.*;

@Slf4j
public class GuiMain {

    public static void main(String[] args) {
        // 1. 네트워크 초기화
        ClientMessageDispatcher networkDispatcher = new ClientMessageDispatcher();
        networkDispatcher.register(LOGIN_SUCCESS, new LoginResponseHandler());
        networkDispatcher.register(LOGOUT_SUCCESS, new LogoutResponseHandler());
        networkDispatcher.register(CHAT_ROOM_CREATE_SUCCESS, new CreateRoomResponseHandler());
        networkDispatcher.register(CHAT_ROOM_LIST_SUCCESS, new ListRoomResponseHandler());
        networkDispatcher.register(CHAT_ROOM_ENTER_SUCCESS, new EnterRoomResponseHandler());
        networkDispatcher.register(CHAT_MESSAGE_SUCCESS, new ChatResponseHandler());
        networkDispatcher.register(PUSH_NEW_MESSAGE, new PushMessageListener());
        networkDispatcher.register(ERROR, new ErrorResponseHandler());

        MessageClient client = new MessageClient(DEFAULT_SERVER_ADDRESS, DEFAULT_SERVER_PORT, networkDispatcher);

        // 2. 서비스 초기화
        UserClientService userClientService = new UserClientService(client);
        ChatRoomClientService chatRoomClientService = new ChatRoomClientService(client);

        // 3. GUI 초기화
        LoginPanel loginPanel = new LoginPanel(userClientService);
        RoomListPanel roomListPanel = new RoomListPanel(userClientService, chatRoomClientService);
        // TODO : ClientSession.currentRoomId로 방 번호 업데이트 및
        //  ClientSession.isInChatRoom으로 방 진입 체크
        RoomChatPanel roomChatPanel = new RoomChatPanel(chatRoomClientService);
        GuiView view = new GuiView(loginPanel, roomListPanel, roomChatPanel);

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
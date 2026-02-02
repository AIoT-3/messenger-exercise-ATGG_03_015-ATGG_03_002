package com.nhnacademy.messenger.client;

import com.nhnacademy.messenger.client.domain.user.controller.UserController;
import com.nhnacademy.messenger.client.domain.user.service.UserClientService;
import com.nhnacademy.messenger.client.event.EventBus;
import com.nhnacademy.messenger.client.network.ClientMessageDispatcher;
import com.nhnacademy.messenger.client.network.MessageClient;
import com.nhnacademy.messenger.client.ui.ClientUiEventListener;
import com.nhnacademy.messenger.client.ui.gui.GuiView;
import com.nhnacademy.messenger.client.ui.gui.panel.LoginPanel;
import com.nhnacademy.messenger.client.ui.gui.panel.RoomChatPanel;
import com.nhnacademy.messenger.client.ui.gui.panel.RoomListPanel;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.tree.RowMapper;

import static com.nhnacademy.messenger.common.config.AppConstant.DEFAULT_SERVER_ADDRESS;
import static com.nhnacademy.messenger.common.config.AppConstant.DEFAULT_SERVER_PORT;

@Slf4j
public class GuiMain {

    public static void main(String[] args) {
        // 1. 이벤트 버스 초기화
        EventBus eventBus = new EventBus();

        // 2. 네트워크 초기화
        ClientMessageDispatcher networkDispatcher = new ClientMessageDispatcher(eventBus);
        networkDispatcher.init("com.nhnacademy.messenger.client.domain");

        MessageClient client = new MessageClient(DEFAULT_SERVER_ADDRESS, DEFAULT_SERVER_PORT, networkDispatcher);

        // 3. 도메인 컨트롤러 초기화
        UserClientService userClientService = new UserClientService(client);
        UserController userController = new UserController(userClientService);

        // 4. GUI 초기화
        LoginPanel loginPanel = new LoginPanel(userController);
        RoomListPanel roomListPanel = new RoomListPanel();
        // TODO : ClientSession.currentRoomId로 방 번호 업데이트 및
        //  ClientSession.isInChatRoom으로 방 진입 체크
        RoomChatPanel roomChatPanel = new RoomChatPanel(0);
        GuiView view = new GuiView(loginPanel, roomListPanel, roomChatPanel);

        // 5. UI 리스너 등록
        ClientUiEventListener uiListener = new ClientUiEventListener(view);
        eventBus.register(uiListener);

        try {
            // 6. 서버 연결 및 앱 시작
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
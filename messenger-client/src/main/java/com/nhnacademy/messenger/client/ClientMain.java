package com.nhnacademy.messenger.client;

import com.nhnacademy.messenger.client.domain.chat.handler.ChatCommandHandler;
import com.nhnacademy.messenger.client.domain.chat.handler.ChatResponseHandler;
import com.nhnacademy.messenger.client.domain.chat.listener.PushMessageListener;
import com.nhnacademy.messenger.client.domain.error.handler.ErrorResponseHandler;
import com.nhnacademy.messenger.client.domain.room.handler.CreateRoomCommandHandler;
import com.nhnacademy.messenger.client.domain.room.handler.CreateRoomResponseHandler;
import com.nhnacademy.messenger.client.domain.room.handler.EnterRoomCommandHandler;
import com.nhnacademy.messenger.client.domain.room.handler.EnterRoomResponseHandler;
import com.nhnacademy.messenger.client.domain.room.handler.ListRoomCommandHandler;
import com.nhnacademy.messenger.client.domain.room.handler.ListRoomResponseHandler;
import com.nhnacademy.messenger.client.domain.room.service.ChatRoomClientService;
import com.nhnacademy.messenger.client.domain.user.handler.LoginCommandHandler;
import com.nhnacademy.messenger.client.domain.user.handler.LoginResponseHandler;
import com.nhnacademy.messenger.client.domain.user.handler.LogoutCommandHandler;
import com.nhnacademy.messenger.client.domain.user.handler.LogoutResponseHandler;
import com.nhnacademy.messenger.client.domain.user.service.UserClientService;
import com.nhnacademy.messenger.client.session.ClientSession;
import com.nhnacademy.messenger.common.event.EventBus;
import com.nhnacademy.messenger.client.network.ClientMessageDispatcher;
import com.nhnacademy.messenger.client.network.MessageClient;
import com.nhnacademy.messenger.client.ui.ClientUiEventListener;
import com.nhnacademy.messenger.client.ui.cli.Command;
import com.nhnacademy.messenger.client.ui.cli.CommandParser;
import com.nhnacademy.messenger.client.ui.cli.ConsoleView;
import com.nhnacademy.messenger.client.ui.cli.dispatcher.CLICommandDispatcher;
import com.nhnacademy.messenger.client.ui.cli.handler.HelpCommandHandler;
import lombok.extern.slf4j.Slf4j;

import static com.nhnacademy.messenger.common.config.AppConstant.*;
import static com.nhnacademy.messenger.common.message.header.MessageType.*;

@Slf4j
public class ClientMain {

    public static void main(String[] args) {

        // 1. 이벤트 버스 및 UI 리스너 초기화
        ConsoleView view = new ConsoleView();
        ClientUiEventListener uiListener = new ClientUiEventListener(view);
        EventBus.INSTANCE.register(uiListener);

        // 2. 네트워크 초기화
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
        CommandParser parser = new CommandParser();

        // 3. 서비스 초기화
        UserClientService userClientService = new UserClientService(client);
        ChatRoomClientService chatRoomClientService = new ChatRoomClientService(client);

        // 4. CLI 명령어 디스패처 초기화
        CLICommandDispatcher cliDispatcher = new CLICommandDispatcher(view);
        cliDispatcher.register(new LoginCommandHandler(userClientService));
        cliDispatcher.register(new LogoutCommandHandler(userClientService));
        cliDispatcher.register(new CreateRoomCommandHandler(chatRoomClientService));
        cliDispatcher.register(new ListRoomCommandHandler(chatRoomClientService));
        cliDispatcher.register(new EnterRoomCommandHandler(chatRoomClientService));
        cliDispatcher.register(new ChatCommandHandler(chatRoomClientService));
        cliDispatcher.register(new HelpCommandHandler(cliDispatcher));

        try {
            // 5. 서버 연결
            client.connect();
            view.showSystemMessage("메신저 클라이언트를 시작합니다. (/login [ID] [PW] 로 로그인하세요)");

            // 6. 입력 루프
            boolean running = true;
            while (running) {
                String input = view.readInput();
                if (input.isEmpty()) continue;

                // 채팅 모드 처리 (일반 텍스트)
                if (!input.startsWith("/")) {
                    Long currentRoomId = ClientSession.INSTANCE.getCurrentRoomId();
                    if (currentRoomId != null) {
                        chatRoomClientService.sendMessage(currentRoomId, input);
                    } else {
                        view.showErrorMessage("활성화된 채팅방이 없습니다. /enter <roomId> 로 입장하거나 /chat 명령어를 사용하세요.");
                    }
                    continue;
                }

                Command command = parser.parse(input);

                if (command.is("/exit") || command.is("/quit")) {
                    running = false;
                } else {
                    cliDispatcher.dispatch(command);
                }
            }

        } catch (Exception e) {
            log.error("클라이언트 실행 중 오류 발생", e);
        } finally {
            client.disconnect();
            view.showSystemMessage("프로그램을 종료합니다.");
        }
    }
}
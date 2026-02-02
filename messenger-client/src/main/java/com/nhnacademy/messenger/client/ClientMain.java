package com.nhnacademy.messenger.client;

import com.nhnacademy.messenger.client.domain.user.controller.UserController;
import com.nhnacademy.messenger.client.domain.user.service.UserClientService;
import com.nhnacademy.messenger.common.event.EventBus;
import com.nhnacademy.messenger.client.network.ClientMessageDispatcher;
import com.nhnacademy.messenger.client.network.MessageClient;
import com.nhnacademy.messenger.client.ui.ClientUiEventListener;
import com.nhnacademy.messenger.client.ui.cli.Command;
import com.nhnacademy.messenger.client.ui.cli.CommandParser;
import com.nhnacademy.messenger.client.ui.cli.ConsoleView;
import com.nhnacademy.messenger.client.ui.cli.dispatcher.CLICommandDispatcher;
import lombok.extern.slf4j.Slf4j;

import static com.nhnacademy.messenger.common.config.AppConstant.DEFAULT_SERVER_ADDRESS;
import static com.nhnacademy.messenger.common.config.AppConstant.DEFAULT_SERVER_PORT;

@Slf4j
public class ClientMain {

    public static void main(String[] args) {

        // 1. 이벤트 버스 및 UI 리스너 초기화
        EventBus eventBus = new EventBus();
        ConsoleView view = new ConsoleView();
        ClientUiEventListener uiListener = new ClientUiEventListener(view);
        eventBus.register(uiListener); // View가 이벤트를 구독

        // 2. 네트워크 초기화
        ClientMessageDispatcher networkDispatcher = new ClientMessageDispatcher(eventBus);
        networkDispatcher.init("com.nhnacademy.messenger.client.domain"); // 네트워크 핸들러 스캔

        MessageClient client = new MessageClient(DEFAULT_SERVER_ADDRESS, DEFAULT_SERVER_PORT, networkDispatcher);
        CommandParser parser = new CommandParser();

        // 3. 도메인 컨트롤러 초기화
        UserClientService userClientService = new UserClientService(client);
        UserController userController = new UserController(userClientService);

        // 4. CLI 명령어 디스패처 초기화
        CLICommandDispatcher cliDispatcher = new CLICommandDispatcher(view);
        cliDispatcher.registerDependency(userController);
        cliDispatcher.init("com.nhnacademy.messenger.client.domain"); // CLI 핸들러 스캔

        try {
            // 5. 서버 연결
            client.connect();
            view.showSystemMessage("메신저 클라이언트를 시작합니다. (/login [ID] [PW] 로 로그인하세요)");

            // 6. 입력 루프
            boolean running = true;
            while (running) {
                String input = view.readInput();
                if (input.isEmpty()) continue;

                Command command = parser.parse(input);

                if (command.is("/exit") || command.is("/quit")) {
                    running = false;
                } else {
                    // 모든 명령어 처리를 디스패처에게 위임
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

package com.nhnacademy.messenger.client.ui.cli.dispatcher;

import com.nhnacademy.messenger.client.ui.cli.Command;
import com.nhnacademy.messenger.client.ui.cli.ConsoleView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class CLICommandDispatcher {

    private final ConsoleView view;
    private final Map<String, CommandExecutable> handlerMap = new HashMap<>();

    public void register(String command, CommandExecutable handler) {
        if (handlerMap.containsKey(command)) {
            log.warn("명령어 핸들러가 교체됩니다: {}", command);
        }
        handlerMap.put(command, handler);
        log.info("CLI 핸들러 등록: {} -> {}", command, handler.getClass().getSimpleName());
    }

    public void dispatch(Command command) {
        Optional.ofNullable(handlerMap.get(command.action()))
                .ifPresentOrElse(
                        handler -> {
                            try {
                                handler.execute(command, view);
                            } catch (Exception e) {
                                log.error("명령어 실행 중 오류: {}", command.action(), e);
                                view.showErrorMessage("명령어 실행 실패");
                            }
                        },
                        () -> {
                            // 핸들러가 없으면 도움말이나 에러
                            if (!command.action().isEmpty()) {
                                view.showErrorMessage("알 수 없는 명령어입니다: " + command.action());
                            }
                        }
                );
    }
}

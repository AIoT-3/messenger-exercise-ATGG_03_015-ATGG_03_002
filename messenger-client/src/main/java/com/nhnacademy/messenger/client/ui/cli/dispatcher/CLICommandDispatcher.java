package com.nhnacademy.messenger.client.ui.cli.dispatcher;

import com.nhnacademy.messenger.client.ui.cli.Command;
import com.nhnacademy.messenger.client.ui.cli.ConsoleView;
import com.nhnacademy.messenger.client.ui.cli.annotation.CommandMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class CLICommandDispatcher {

    private final ConsoleView view;
    private final Map<Class<?>, Object> dependencyContext = new HashMap<>();
    private final Map<String, CommandExecutable> handlerMap = new HashMap<>();

    public void registerDependency(Object dependency) {
        dependencyContext.put(dependency.getClass(), dependency);
    }

    public void init(String basePackage) {
        log.debug("CLI 명령어 핸들러 스캔 시작 (패키지: {})", basePackage);
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackages(basePackage)
                .addScanners(Scanners.TypesAnnotated));
        
        reflections.getTypesAnnotatedWith(CommandMapping.class).stream()
                .filter(clazz -> {
                    if (CommandExecutable.class.isAssignableFrom(clazz)) {
                        return true;
                    }
                    log.warn("@CommandMapping이 있지만 CommandExecutable을 구현하지 않음: {}", clazz.getName());
                    return false;
                })
                .forEach(this::registerHandler);
    }

    private void registerHandler(Class<?> clazz) {
        try {
            CommandMapping mapping = clazz.getAnnotation(CommandMapping.class);
            CommandExecutable handler = createHandlerInstance(clazz);
            handlerMap.put(mapping.command(), handler);
        } catch (Exception e) {
            log.error("CLI 핸들러 생성 실패: {}", clazz.getName(), e);
        }
    }

    private CommandExecutable createHandlerInstance(Class<?> clazz) throws Exception {
        // 생성자 확인
        Constructor<?>[] constructors = clazz.getConstructors();
        if (constructors.length == 0) {
            throw new IllegalStateException("public 생성자가 없습니다: " + clazz.getName());
        }
        
        // 첫 번째 생성자 사용 (단순화)
        Constructor<?> constructor = constructors[0];
        Class<?>[] paramTypes = constructor.getParameterTypes();
        Object[] args = new Object[paramTypes.length];

        // 파라미터 타입에 맞는 의존성 주입
        for (int i = 0; i < paramTypes.length; i++) {
            Class<?> type = paramTypes[i];
            Object dependency = dependencyContext.get(type);
            
            if (dependency == null) {
                // 정확히 일치하는 타입이 없으면 상속 관계 확인 (예: AuthController가 필요한데 Object로 등록된 경우 등은 배제하고 단순 매칭)
                // 이번 구현에서는 정확한 타입 매칭만 지원
                log.warn("핸들러 {} 생성 중 의존성을 찾을 수 없습니다: {}", clazz.getSimpleName(), type.getName());
                // null 주입 시도 (생성자에서 null 체크하면 터짐)
            }
            args[i] = dependency;
        }

        return (CommandExecutable) constructor.newInstance(args);
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

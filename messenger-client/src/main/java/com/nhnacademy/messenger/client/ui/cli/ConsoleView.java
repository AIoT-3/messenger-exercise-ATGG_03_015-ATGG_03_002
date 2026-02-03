package com.nhnacademy.messenger.client.ui.cli;

import com.nhnacademy.messenger.client.ui.View;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class ConsoleView implements View {

    private final Scanner scanner;
    private final PrintStream out;

    public ConsoleView() {
        this.scanner = new Scanner(System.in);
        this.out = System.out;
    }

    // --- View 인터페이스 구현 ---

    @Override
    public void showSystemMessage(String message) {
        out.println("[시스템] " + message);
    }

    @Override
    public void showErrorMessage(String message) {
        out.println("[오류] " + message);
    }

    @Override
    public void showLoginSuccess(String userName) {
        out.println("환영합니다, " + userName + "님!");
    }

    @Override
    public void showRoomList(List<String> rooms) {
        out.println("============== 채팅방 목록 ==============");
        if (rooms == null || rooms.isEmpty()) {
            out.println("(채팅방이 없습니다)");
        } else {
            for (String room : rooms) {
                out.println("- " + room);
            }
        }
        out.println("========================================");
    }

    @Override
    public void showRoomEnterSuccess(Long roomId, List<String> users) {
        out.println(">> [" + roomId + "] 번 방에 입장했습니다.");
        out.println("참여자: " + String.join(", ", users));
    }

    @Override
    public void appendMessage(String sender, String content) {
        // [보낸이]: 내용 형식
        out.println("[" + sender + "]: " + content);
    }

    // --- CLI 전용 기능 ---

    public String readInput() {
        out.print("> ");
        out.flush();
        String line = scanner.nextLine();
        return StringUtils.trim(line);
    }
}

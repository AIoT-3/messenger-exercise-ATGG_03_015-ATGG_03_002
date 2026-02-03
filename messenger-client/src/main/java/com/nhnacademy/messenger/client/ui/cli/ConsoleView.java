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
    public void showLogoutSuccess() {
        out.println("[로그아웃] 로그아웃 되었습니다. /login [ID] [PW] 로 로그인하세요");
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
    public void showRoomEnterSuccess(String roomName) {
        out.println(">> [" + roomName + "] 방에 입장했습니다.");
    }

    @Override
    public void appendMessage(String sender, String content) {
        // [보낸이]: 내용 형식
        out.println("[" + sender + "]: " + content);
    }

    // --- CLI 전용 기능 ---

    public String readInput() {
        out.print("> ");
        String line = scanner.nextLine();
        return StringUtils.trim(line);
    }
}

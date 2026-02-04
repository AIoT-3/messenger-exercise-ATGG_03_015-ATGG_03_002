package com.nhnacademy.messenger.client.ui.cli;

import com.nhnacademy.messenger.client.ui.View;
import com.nhnacademy.messenger.common.message.data.room.RoomInfo;
import com.nhnacademy.messenger.common.message.data.user.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class ConsoleView implements View {

    private final Scanner scanner;
    private final PrintStream out;
    private final java.util.Map<Long, String> roomNameMap = new java.util.HashMap<>();

    public ConsoleView() {
        this.scanner = new Scanner(System.in);
        this.out = System.out;
    }

    // --- Helpers ---

    private void printWithPrompt(String message) {
        // 현재 줄의 프롬프트를 덮어쓰기 위해 \r 사용
        out.print("\r" + message + "\n> ");
        out.flush();
    }

    // --- View 인터페이스 구현 ---

    @Override
    public void showSystemMessage(String message) {
        printWithPrompt("[시스템] " + message);
    }

    @Override
    public void showErrorMessage(String message) {
        printWithPrompt("[오류] " + message);
    }

    @Override
    public void showLoginSuccess(String userName) {
        printWithPrompt("환영합니다, " + userName + "님! (명령어 목록을 보려면 /help를 입력하세요)");
    }

    @Override
    public void showLogoutSuccess() {
        printWithPrompt("[로그아웃] 로그아웃 되었습니다. /login [ID] [PW] 로 로그인하세요");
    }

    @Override
    public void showUserList(List<UserInfo> users) {
        StringBuilder sb = new StringBuilder();
        sb.append("============== 사용자 목록 ==============\n");
        if (users == null || users.isEmpty()) {
            sb.append("(사용자가 없습니다)\n");
        } else {
            for (UserInfo user : users) {
                String status = user.online() ? "[+]" : "[ ]";
                sb.append(String.format("- %s %s (%s)%n", status, user.name(), user.id()));
            }
        }
        sb.append("=======================================");
        printWithPrompt(sb.toString());
    }

    @Override
    public void showRoomList(List<RoomInfo> rooms) {
        StringBuilder sb = new StringBuilder();
        sb.append("============== 채팅방 목록 ==============\n");
        if (rooms == null || rooms.isEmpty()) {
            sb.append("(채팅방이 없습니다)\n");
        } else {
            for (RoomInfo info : rooms) {
                roomNameMap.put(info.roomId(), info.roomName());
                sb.append(String.format("- [%d] %s (인원: %d명)%n",
                        info.roomId(), info.roomName(), info.userCount()));
            }
        }
        sb.append("=======================================");
        printWithPrompt(sb.toString());
    }

    @Override
    public void showRoomEnterSuccess(Long roomId, List<String> users) {
        String roomName = roomNameMap.getOrDefault(roomId, String.valueOf(roomId));
        String msg = ">> [" + roomName + "] 방에 입장했습니다.\n참여자: " + String.join(", ", users);
        printWithPrompt(msg);
    }

    @Override
    public void showRoomExitSuccess(Long roomId) {
        out.println(">> [" + roomId + "] 번 방에서 퇴장했습니다.");
    }

    @Override
    public void appendMessage(Long roomId, String sender, String content) {
        // TODO: 여러 채팅방에 속한 경우 어떻게 메세지를 출력할지 고민
        printWithPrompt("[" + sender + "]: " + content);
    }

    // --- CLI 전용 기능 ---

    public String readInput() {
        out.print("\r> ");
        out.flush();
        if (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            return StringUtils.trim(line);
        }
        return "";
    }
}

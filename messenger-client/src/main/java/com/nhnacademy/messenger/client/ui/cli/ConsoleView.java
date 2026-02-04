package com.nhnacademy.messenger.client.ui.cli;

import com.nhnacademy.messenger.client.ui.View;
import com.nhnacademy.messenger.common.message.data.chat.MessageInfo;
import com.nhnacademy.messenger.common.message.data.room.RoomInfo;
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
    public void showRoomList(List<RoomInfo> rooms) {
        StringBuilder sb = new StringBuilder();
        sb.append("============== 채팅방 목록 ==============\n");
        if (rooms == null || rooms.isEmpty()) {
            sb.append("(채팅방이 없습니다)\n");
        } else {
            for (RoomInfo info : rooms) {
                sb.append(String.format("- [%d] %s (인원: %d명)%n",
                        info.roomId(), info.roomName(), info.userCount()));
            }
        }
        sb.append("=======================================");
        printWithPrompt(sb.toString());
    }

    @Override
    public void showRoomEnterSuccess(Long roomId, List<String> users) {
        String msg = ">> [" + roomId + "] 번 방에 입장했습니다.\n참여자: " + String.join(", ", users);
        printWithPrompt(msg);
    }

    @Override
    public void showRoomExitSuccess(Long roomId) {
        out.println(">> [" + roomId + "] 번 방에서 퇴장했습니다.");
    }

    @Override
    public void appendMessage(Long roomId, Long messageId, String sender, String content) {
        // TODO: 여러 채팅방에 속한 경우 어떻게 메세지를 출력할지 고민
        printWithPrompt(String.format("[ID: %d] [%s]: %s", messageId, sender, content));
    }

    @Override
    public void showChatHistory(Long roomId, List<MessageInfo> messages, boolean hasMore) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n----------- 과거 채팅 기록 -----------").append("\n");
        for (MessageInfo msg : messages) {
            sb.append(String.format("[ID: %d] [%s]: %s", msg.messageId(), msg.senderName(), msg.content())).append("\n");
        }
        if (hasMore) {
            sb.append("(이전 기록이 더 존재합니다. /history <roomId> <limit> <beforeMessageId> 명령어로 더 볼 수 있습니다.)").append("\n");
        }
        sb.append("------------------------------------");
        printWithPrompt(sb.toString());
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

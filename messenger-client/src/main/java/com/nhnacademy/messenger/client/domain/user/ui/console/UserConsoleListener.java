package com.nhnacademy.messenger.client.domain.user.ui.console;

import com.nhnacademy.messenger.client.domain.user.event.LoginSuccessEvent;
import com.nhnacademy.messenger.client.domain.user.event.LogoutSuccessEvent;
import com.nhnacademy.messenger.client.domain.user.event.UserListSuccessEvent;
import com.nhnacademy.messenger.client.ui.cli.ConsoleView;
import com.nhnacademy.messenger.common.event.EventListener;
import com.nhnacademy.messenger.common.message.data.user.UserInfo;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserConsoleListener {

    private final ConsoleView view;

    @EventListener
    public void onLoginSuccess(LoginSuccessEvent event) {
        view.println("환영합니다, " + event.userId() + "님! (명령어 목록을 보려면 /help를 입력하세요)");
    }

    @EventListener
    public void onLogoutSuccess(LogoutSuccessEvent event) {
        view.println("[로그아웃] 로그아웃 되었습니다. /login [ID] [PW] 로 로그인하세요");
    }

    @EventListener
    public void onUserListReceived(UserListSuccessEvent event) {
        List<UserInfo> users = event.userList();
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
        view.println(sb.toString());
    }
}
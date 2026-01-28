package com.nhnacademy.messenger.client.ui;

import com.nhnacademy.messenger.common.data.room.list.ChatRoomListResponse;
import com.nhnacademy.messenger.common.data.user.list.UserListResponse;
import java.util.List;

public interface View {

    void start();

    void showLoginSuccess(String userId);
    void showLoginFailure(String reason);
    void showLogoutSuccess();

    void showRoomList(List<ChatRoomListResponse.RoomInfo> roomList);
    void showUserList(List<UserListResponse.UserInfo> userList);

    void showCreateRoomSuccess(Long roomId, String roomName);
    void showEnterRoomSuccess(Long roomId, List<String> currentMembers);
    void showEnterRoomFailure(String reason);
    void showExitRoomSuccess(Long roomId);

    void showNewMessage(Long roomId, String senderId, String content);

    void showPrivateMessage(String senderId, String content);

    void showError(String errorMessage);
    void showSystemMessage(String message);
}
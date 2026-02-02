package com.nhnacademy.messenger.client.session;

import java.util.concurrent.atomic.AtomicReference;

public enum ClientSession {
    INSTANCE;

    private final AtomicReference<String> sessionId = new AtomicReference<>();
    private final AtomicReference<String> userId = new AtomicReference<>();
    private final AtomicReference<String> userName = new AtomicReference<>();
    private final AtomicReference<String> currentRoomId = new AtomicReference<>();

    public String getSessionId() {
        return sessionId.get();
    }

    public void setSessionId(String sessionId) {
        this.sessionId.set(sessionId);
    }

    public String getUserId() {
        return userId.get();
    }

    public void setUserId(String userId) {
        this.userId.set(userId);
    }

    public String getUserName() {
        return userName.get();
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    public String getCurrentRoomId() {
        return currentRoomId.get();
    }

    public void setCurrentRoomId(String currentRoomId) {
        this.currentRoomId.set(currentRoomId);
    }

    public boolean isLoggedIn() {
        return sessionId.get() != null;
    }

    public boolean isInChatRoom() {
        return currentRoomId.get() != null;
    }

    public void clear() {
        this.sessionId.set(null);
        this.userId.set(null);
        this.userName.set(null);
        this.currentRoomId.set(null);
    }
}
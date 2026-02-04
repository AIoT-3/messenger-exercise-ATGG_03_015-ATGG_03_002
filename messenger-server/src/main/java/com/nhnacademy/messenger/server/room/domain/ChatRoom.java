package com.nhnacademy.messenger.server.room.domain;

import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.server.session.domain.Session;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Getter
@Builder
public class ChatRoom {
    private Long roomId;
    private String roomName;
    @Builder.Default
    private final Set<Session> sessions = new CopyOnWriteArraySet<>();

    public void addSession(Session session) {
        sessions.add(session);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }

    public void broadcast(Message message) {
        for (Session session : sessions) {
            try {
                session.sendMessage(message);
            } catch (Exception e) {
                log.warn("broadcast 전송 실패, 세션 종료: {}", session.getId(), e);
                session.disconnect();
            }
        }
    }

    public void assignRoomId(Long roomId) {
        this.roomId = roomId;
    }
}

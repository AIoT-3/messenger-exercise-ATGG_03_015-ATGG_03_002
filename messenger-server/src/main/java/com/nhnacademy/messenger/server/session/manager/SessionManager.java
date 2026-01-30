package com.nhnacademy.messenger.server.session.manager;

import com.nhnacademy.messenger.server.session.domain.Session;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    // Key: sessionId, Value: Session
    private final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    public void addSession(Session session) {
        sessionMap.put(session.getId(), session);
    }

    public void removeSession(String sessionId) {
        sessionMap.remove(sessionId);
    }

    public Optional<Session> getSession(String sessionId) {
        return Optional.ofNullable(sessionMap.get(sessionId));
    }
}

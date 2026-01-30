package com.nhnacademy.messenger.server.session.manager;

import com.nhnacademy.messenger.server.session.domain.Session;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    // Key: sessionId, Value: Session
    private final Map<String, Session> sessionMap = new ConcurrentHashMap<>();
    // Key: userId, Value: Session
    private final Map<String, Session> userSessionMap = new ConcurrentHashMap<>();

    public void addSession(Session session) {
        sessionMap.put(session.getId(), session);
        if (session.getUser() != null) {
            userSessionMap.put(session.getUser().getUserId(), session);
        }
    }

    public void removeSession(String sessionId) {
        Session removed = sessionMap.remove(sessionId);
        if (removed != null && removed.getUser() != null) {
            userSessionMap.remove(removed.getUser().getUserId(), removed);
        }
    }

    public Optional<Session> getSession(String sessionId) {
        return Optional.ofNullable(sessionMap.get(sessionId));
    }

    public Optional<Session> getSessionByUserId(String userId) {
        return Optional.ofNullable(userSessionMap.get(userId));
    }
}

package com.nhnacademy.messenger.server.session.manager;

import com.nhnacademy.messenger.server.session.domain.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    // Key: userId, Value: Session
    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    public void addSession(Session session) {

    }

    public void removeSession(String userId) {

    }

    public Session getSession(String userId) {
        return null;
    }

    public void broadcast(Object message) {

    }
}

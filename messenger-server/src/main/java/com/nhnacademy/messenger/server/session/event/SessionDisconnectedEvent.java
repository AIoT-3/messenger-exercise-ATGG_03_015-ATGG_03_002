package com.nhnacademy.messenger.server.session.event;

import com.nhnacademy.messenger.server.session.domain.Session;

public record SessionDisconnectedEvent(
        Session session
) {
}

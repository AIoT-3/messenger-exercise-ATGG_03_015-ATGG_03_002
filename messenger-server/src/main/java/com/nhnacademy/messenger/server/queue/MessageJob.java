package com.nhnacademy.messenger.server.queue;

import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.server.session.domain.Session;

/**
 * MessageJob
 * 역할: 큐에 적재될 작업 단위 (어떤 세션에서 어떤 메시지가 왔는지 저장)
 */
public record MessageJob(
        Session session,
        Message message
) {
}

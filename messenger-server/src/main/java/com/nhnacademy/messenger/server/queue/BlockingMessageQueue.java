package com.nhnacademy.messenger.server.queue;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * BlockingMessageQueue
 * 역할: 메시지 처리 요청을 순차적으로 쌓아두는 대기열
 */
@Slf4j
public class BlockingMessageQueue {

    private final BlockingQueue<MessageJob> queue;

    public BlockingMessageQueue() {
        this.queue = new LinkedBlockingQueue<>();
    }

    public void put(MessageJob job) {
        try {
            queue.put(job);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("메시지 큐 삽입 중 인터럽트 발생", e);
        }
    }

    public MessageJob take() throws InterruptedException {
        return queue.take();
    }

    public int size() {
        return queue.size();
    }
}

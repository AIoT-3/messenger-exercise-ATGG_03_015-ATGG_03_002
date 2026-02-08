package com.nhnacademy.messenger.server.queue;

import com.nhnacademy.messenger.common.exception.MessageConvertException;
import com.nhnacademy.messenger.common.exception.MessengerException;
import com.nhnacademy.messenger.common.message.data.error.ErrorCode;
import com.nhnacademy.messenger.server.network.MessageDispatcher;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * MessageWorker
 * 역할: 큐에서 메시지를 꺼내 비즈니스 로직(Dispatcher)을 실행함
 */
@Slf4j
public class MessageWorker implements Runnable {

    private final BlockingMessageQueue messageQueue;
    private final MessageDispatcher messageDispatcher;
    private final ExecutorService businessExecutor;

    public MessageWorker(BlockingMessageQueue messageQueue, MessageDispatcher messageDispatcher) {
        this.messageQueue = messageQueue;
        this.messageDispatcher = messageDispatcher;
        this.businessExecutor = Executors.newVirtualThreadPerTaskExecutor();
    }

    @Override
    public void run() {
        log.info("MessageWorker 기동 완료 (가상 스레드 기반)");
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // 1. 큐에서 작업이 들어올 때까지 대기(Blocking)
                MessageJob job = messageQueue.take();

                // 2. 개별 작업 처리를 가상 스레드에 위임 (Worker 루프는 즉시 다음 작업을 가져옴)
                businessExecutor.submit(() -> handleJob(job));
            }
        } catch (InterruptedException e) {
            log.info("MessageWorker 중단됨");
            Thread.currentThread().interrupt();
        } finally {
            businessExecutor.shutdown();
        }
    }

    private void handleJob(MessageJob job) {
        job.session().processRequest(job.message());
    }
}

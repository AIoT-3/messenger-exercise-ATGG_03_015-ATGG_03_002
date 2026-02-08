package com.nhnacademy.messenger.server.network;

import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.server.queue.BlockingMessageQueue;
import com.nhnacademy.messenger.server.queue.MessageJob;
import com.nhnacademy.messenger.server.session.domain.NioSession;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

/**
 * NioWorker
 * 역할:
 * 1. 자신에게 할당된 세션들의 I/O 이벤트를 감시
 * 2. 메시지가 완성되면 큐에 적재
 */
@Slf4j
public class NioWorker implements Runnable {

    private final int id;
    private final Selector selector;
    private final BlockingMessageQueue messageQueue;

    public NioWorker(int id, BlockingMessageQueue messageQueue) throws IOException {
        this.id = id;
        this.messageQueue = messageQueue;
        this.selector = Selector.open();
    }

    //새로운 세션을 자신의 셀렉터에 등록
    public void register(NioSession session) {
        try {
            session.register(selector);
            selector.wakeup(); // 차단된 select()를 깨워 즉시 이벤트 감시 시작
        } catch (IOException e) {
            log.error("Worker-{} 세션 등록 실패", id, e);
        }
    }

    @Override
    public void run() {
        log.debug("NioWorker-{} 기동", id);
        try {
            while (!Thread.currentThread().isInterrupted()) {
                selector.select();
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();
                    if (key.isValid() && key.isReadable()) {
                        read((NioSession) key.attachment());
                    }
                }
            }
        } catch (IOException e) {
            log.error("NioWorker-{} 실행 중 오류", id, e);
        }
    }

    private void read(NioSession session) {
        try {
            // 메시지가 조립될 때까지 읽기 시도
            Message message = session.readMessage();
            if (message != null) {
                // 메시지가 완성되면 공유 대기열에 적재
                messageQueue.put(new MessageJob(session, message));
            }
        } catch (IOException e) {
            // 연결 종료 또는 네트워크 오류 시 세션 정리
            session.disconnect();
        } catch (Exception e) {
            log.warn("NioWorker-{} 데이터 수신 처리 중 경고: {}", id, e.getMessage());
        }
    }
}

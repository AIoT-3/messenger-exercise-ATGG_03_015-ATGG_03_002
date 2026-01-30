package com.nhnacademy.messenger.client.net;

import com.nhnacademy.messenger.client.service.MessageDispatcher;
import com.nhnacademy.messenger.common.exception.MessageConvertException;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.util.reader.bio.StreamMessageReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

public class MessageReceiver implements Runnable {
    private final InputStream inputStream;
    private final StreamMessageReader messageReader; // common 모듈의 스트림 리더
    private final MessageDispatcher messageDispatcher; // 클라이언트 service 모듈의 디스패처

    // InputStream과 MessageDispatcher를 외부에서 주입받습니다.
    public MessageReceiver(InputStream inputStream, MessageDispatcher messageDispatcher) {
        this.inputStream = inputStream;
        this.messageReader = new StreamMessageReader(inputStream); // common 모듈의 StreamMessageReader 초기화
        this.messageDispatcher = messageDispatcher;
    }

    @Override
    public void run() {
        System.out.println("[MessageReceiver] started.");
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Message receivedMessage = messageReader.read();

                if (receivedMessage != null) {
                    System.out.println("[MessageReceiver] Received: " + receivedMessage.header());
                    // 2. 수신된 Message를 MessageDispatcher에 전달하여 적절한 Service의 핸들러로 라우팅
                    messageDispatcher.dispatch(receivedMessage);
                }
            }
        } catch (SocketException e) {
            System.err.println("[MessageReceiver] Socket closed or connection reset: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("[MessageReceiver] Error reading message: " + e.getMessage());
        } catch (MessageConvertException e) {
            System.err.println("[MessageReceiver] Error converting received message: " + e.getMessage());
        } finally {
            System.out.println("[MessageReceiver] stopped.");
            // TODO: 스레드 종료 시 연결 정리 또는 재연결 시도 로직
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                System.err.println("[MessageReceiver] Error closing input stream: " + e.getMessage());
            }
        }
    }
}

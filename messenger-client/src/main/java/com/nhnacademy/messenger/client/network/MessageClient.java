package com.nhnacademy.messenger.client.network;

import com.nhnacademy.messenger.common.exception.MessageConvertException;
import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.util.reader.bio.StreamMessageReader;
import com.nhnacademy.messenger.common.util.writer.bio.StreamMessageWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

@Slf4j
public class MessageClient {

    private final String host;
    private final int port;
    private final ClientMessageDispatcher dispatcher;
    
    private Socket socket;
    private StreamMessageReader reader;
    private StreamMessageWriter writer;
    private volatile boolean active = false;

    public MessageClient(String host, int port, ClientMessageDispatcher dispatcher) {
        this.host = host;
        this.port = port;
        this.dispatcher = dispatcher;
    }

    public void connect() {
        if (active) {
            return;
        }

        try {
            this.socket = new Socket(host, port);
            this.reader = new StreamMessageReader(socket.getInputStream());
            this.writer = new StreamMessageWriter(socket.getOutputStream());
            this.active = true;
            
            Thread.ofVirtual()
                    .name("Client-Receiver")
                    .start(this::receiveLoop);
        } catch (IOException e) {
            this.active = false;
            throw new RuntimeException("서버 연결 실패: " + host + ":" + port, e);
        }
    }

    public void send(Message message) {
        if (!active || writer == null) {
            throw new IllegalStateException("서버와 연결되어 있지 않습니다.");
        }
        writer.writeMessage(message);
    }

    public void disconnect() {
        if (!active) {
            return;
        }

        this.active = false;
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
                log.info("서버와의 연결이 종료되었습니다.");
            } catch (IOException e) {
                log.error("소켓 종료 중 오류 발생", e);
            }
        }
    }

    private void receiveLoop() {
        try {
            while (active && !Thread.currentThread().isInterrupted()) {
                try {
                    Message message = reader.readMessage();
                    dispatcher.dispatch(message);

                } catch (MessageConvertException e) {
                    log.warn("메시지 변환 오류: {}", e.getMessage());
                    // 연결을 유지하면서 다음 메시지 수신 시도

                } catch (EOFException e) {
                    log.debug("서버와의 연결이 종료되었습니다.");
                    disconnect();
                    break;
                    
                } catch (Exception e) {
                    if (active) {
                        log.error("메시지 수신 중 오류 또는 연결 종료: {}", e.getMessage());
                        disconnect(); 
                    }
                    break;
                }
            }
        } catch (Exception e) {
            log.error("수신 스레드 비정상 종료", e);
        } finally {
            disconnect();
        }
    }

    public boolean isConnected() {
        return active && socket != null && !socket.isClosed() && socket.isConnected();
    }
}

package com.nhnacademy.messenger.client.net;

import com.nhnacademy.messenger.common.message.Message;
import com.nhnacademy.messenger.common.util.converter.MessageConverter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerConnector {
    private final Socket socket;
    private final OutputStream outputStream;

    public ServerConnector(String serverAddress, int serverPort) throws IOException {
        this.socket = new Socket(serverAddress, serverPort);
        this.outputStream = socket.getOutputStream();
        System.out.println("[ServerConnector] Connected to server: " + serverAddress + ":" + serverPort);
    }

    /**
     * Message 객체를 JSON 형태로 변환하여 서버로 전송합니다.
     * @param message 전송할 Message 객체
     */
    public void send(Message message) {
        try {
            String jsonMessage = MessageConverter.serializeMessage(message);
            outputStream.write(jsonMessage.getBytes());
            outputStream.flush();

        } catch (IOException e) {
            System.err.println("[ServerConnector] Error sending message: " + e.getMessage());
            // TODO: 에러 처리
        }
    }

    public InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    public void close() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("[ServerConnector] Error closing socket: " + e.getMessage());
        }
    }
}

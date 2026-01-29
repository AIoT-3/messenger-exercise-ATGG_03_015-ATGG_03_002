package com.nhnacademy.messenger.server.runnable;

import com.nhnacademy.messenger.common.config.AppConstant;

import java.io.IOException;
import java.net.ServerSocket;

public class MessageServer implements Runnable{

    private final ServerSocket serverSocket;

    public MessageServer() {
        this(AppConstant.DEFAULT_SERVER_PORT);
    }

    public MessageServer(int port) {
        if (port <= 0) {
            throw new IllegalArgumentException(String.format("port:%d", port));
        }

        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void run() {

    }
}

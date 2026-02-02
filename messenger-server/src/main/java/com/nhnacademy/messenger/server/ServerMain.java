package com.nhnacademy.messenger.server;

import com.nhnacademy.messenger.server.network.MessageServer;

public class ServerMain {

    public static void main(String[] args) {
        MessageServer messageServer = new MessageServer();
        messageServer.run();
    }
}

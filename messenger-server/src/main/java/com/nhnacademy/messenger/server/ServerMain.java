package com.nhnacademy.messenger.server;

import com.nhnacademy.messenger.server.network.MessageServer;
import com.nhnacademy.messenger.server.network.NioMessageServer;

public class ServerMain {

    public static void main(String[] args) {
        // MessageServer messageServer = new MessageServer();
        NioMessageServer messageServer = new NioMessageServer();
        messageServer.run();
    }
}

package com.tedBilgar.socket_v2_0.server;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ServerReadThread extends Thread {

    private static final Set<String> specialCommands = new HashSet<>(Arrays.asList("-exit", "-ul"));

    private ChatServer chatServer;

    ServerReadThread(ChatServer chatServer) {
        this.chatServer = chatServer;
    }

    public void run() {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String serverCommand = scanner.nextLine();
            if (specialCommands.contains(serverCommand)) {
                doSpecialCommands(serverCommand);
            }
        }
    }

    private void doSpecialCommands(String command) {
        switch (command) {
            case "-ul": {
                System.out.printf("User list: %s\n", chatServer.getUserNames());
            }
        }
    }
}

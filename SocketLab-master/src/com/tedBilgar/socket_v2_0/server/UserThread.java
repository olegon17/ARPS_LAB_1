package com.tedBilgar.socket_v2_0.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class UserThread extends Thread {

    private static final Set<String> specialCommands = new HashSet<>(Arrays.asList("-exit", "-ul", "-cr", "-chr"));

    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;
    private String room;

    UserThread(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
        this.room = "default";
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            writer = new PrintWriter(output, true);

            printUsers();

            String userName = reader.readLine();
            server.addUserName(userName);

            String serverMessage = String.format("The user [%s] connected at %tc %n\n", userName, ZonedDateTime.now());
            server.broadcast(serverMessage, this, this.room);

            String clientMessage;
            boolean isExit = false;

            do {
                clientMessage = reader.readLine();
                String[] clientMessageCommands = clientMessage.split("\\s+");
                if (clientMessageCommands.length > 0 && !specialCommands.contains(clientMessageCommands[0])) {
                    serverMessage = "[" + userName + "]: " + clientMessage;
                    server.broadcast(serverMessage, this, this.room);
                } else {
                    isExit = doSpecialCommandsAndReturnIsExit(clientMessage);
                }

            } while (!isExit);

            server.removeUser(userName, this);
            socket.close();

            serverMessage = String.format("The user [%s] quited at %tc %n\n", userName, ZonedDateTime.now());
            server.broadcast(serverMessage, this, this.room);

        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private boolean doSpecialCommandsAndReturnIsExit(String specialCommand) {
        boolean isExit = false;
        String[] clientMessageCommands = specialCommand.split("\\s+");
        switch (clientMessageCommands[0]) {
            case "-exit": {
                isExit = true;
                break;
            }
            case "-ul": {
                server.peerToPeer(String.format("User list: %s", server.getUserNames()), this);
                break;
            }
            case "-cr": {
                if (clientMessageCommands.length < 2) {
                    String defaultName = "default_" + LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                    server.peerToPeer(defaultName, this);
                    server.addRoom("default_" + LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                }
                if (clientMessageCommands.length == 2) {
                    server.addRoom(clientMessageCommands[1]);
                    server.peerToPeer(String.format("New room successfully done: %s\n", clientMessageCommands[1]), this);

                }
                break;
            }
            case "-chr": {
                if (clientMessageCommands.length == 2 && server.getRooms().contains(clientMessageCommands[1])) {
                    this.room = clientMessageCommands[1];
                    server.peerToPeer(String.format("You are in [%s] room\n", clientMessageCommands[1]), this);

                } else {
                    server.peerToPeer("Incorrect input for change room\n", this);
                }
                break;
            }
        }
        return isExit;
    }

    /**
     * Sends a list of online users to the newly connected user.
     */
    private void printUsers() {
        if (server.hasUsers()) {
            writer.println("Connected users: " + server.getUserNames());
        } else {
            writer.println("No other users connected");
        }
    }

    /**
     * Sends a message to the client.
     */
    void sendMessage(String message) {
        writer.println(message);
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}

package com.tedBilgar.socket_v2_0.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

public class ChatServer {

    private int port;
    private Set<String> userNames = new HashSet<>();
    private Set<UserThread> userThreads = new HashSet<>();
    private Set<String> rooms = new HashSet<>();

    private ChatServer(int port) {
        this.port = port;
        rooms.add("default");
        new ServerReadThread(this).start();
    }

    private void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Chat Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");

                UserThread newUser = new UserThread(socket, this);
                userThreads.add(newUser);
                newUser.start();

            }

        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntax: java ChatServer <port-number>");
            System.exit(0);
        }

        int port = Integer.parseInt(args[0]);

        ChatServer server = new ChatServer(port);
        server.execute();
    }

    /**
     * Delivers a message from one user to others (broadcasting)
     */
    void broadcast(String message, UserThread excludeUser, String room) {
        for (UserThread user : userThreads) {
            if (user.getRoom().equals(room) && user != excludeUser) {
                user.sendMessage(message);
            }
        }
    }

    void peerToPeer(String message, UserThread user) {
        user.sendMessage(message);
    }

    /**
     * Stores username of the newly connected client.
     */
    void addUserName(String userName) {
        userNames.add(userName);
    }

    /**
     * When a client is disconneted, removes the associated username and UserThread
     */
    void removeUser(String userName, UserThread userThread) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            userThreads.remove(userThread);
            System.out.printf("The user [%s] quited at %tc %n\n", userName, ZonedDateTime.now());
        }
    }

    Set<String> getUserNames() {
        return this.userNames;
    }

    /**
     * Returns true if there are other users connected (not count the currently connected user)
     */
    boolean hasUsers() {
        return !this.userNames.isEmpty();
    }

    public Set<String> getRooms() {
        return rooms;
    }

    public void addRoom(String name) {
        rooms.add(name);
    }
}

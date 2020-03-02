package com.tedBilgar.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

public class Server {

    private final int PORT = 9070;
    private String userName;

    private DataInputStream in;
    private DataOutputStream out;
    private Socket client;

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void run() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)){

            client = serverSocket.accept();

            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());

            while (!client.isClosed()) {
                String message = in.readUTF();

                if (userName != null) {
                    System.out.printf("%s: %s\n", userName, message);
                    String serverMessage = String.format("%s/%s", "echo", message);
                    System.out.printf("Server: %s\n", serverMessage);

                    if (message.equalsIgnoreCase("quit")) {
                        break;
                    }
                    out.writeUTF(serverMessage);
                    out.flush();
                } else {
                    userName = message;
                    System.out.printf("User \"%s\" has been entered at %tc %n\n", userName, ZonedDateTime.now());
                }
            }
        } catch (EOFException e) {
            System.out.printf("User \"%s\" has been left at %tc %n\n", userName, ZonedDateTime.now());

            in.close();
            out.close();

            client.close();
            System.out.println("Server Job is done");
        }
    }
}

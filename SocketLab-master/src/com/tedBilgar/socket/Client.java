package com.tedBilgar.socket;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.util.Scanner;

public class Client {
    private static final int PORT = 9070;
    private static final String HOST = "localhost";
    private static String userName = "noName";

    public static void main(String[] args) throws InterruptedException {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Hello, type your name \n");
        userName = scanner.nextLine();

        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
             DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
             DataInputStream ois = new DataInputStream(socket.getInputStream());){

            oos.writeUTF(userName);
            oos.flush();
            System.out.printf("User \"%s\" has been entered at %tc %n\n", userName, ZonedDateTime.now());

            while (!socket.isOutputShutdown()) {
                if (br.ready()) {
                    String userMessage = br.readLine();

                    if (userMessage.equalsIgnoreCase("quit")) {
                        System.out.printf("User \"%s\" has been left at %tc %n\n", userName, ZonedDateTime.now());
                        break;
                    }
                    oos.writeUTF(userMessage);
                    oos.flush();
                    System.out.printf("%s: %s\n", userName, userMessage);
                    System.out.printf("Server: %s\n", ois.readUTF());
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

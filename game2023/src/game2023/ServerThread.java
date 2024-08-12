package game2023;

import java.net.*;
import java.io.*;
import javafx.application.Application;

public class ServerThread extends Thread {
    Socket connSocket;
    int index;
    Server server;
    DataOutputStream outToClient;
    BufferedReader inFromClient;

    public ServerThread(Socket connSocket, int index, Server server) throws IOException {
        this.connSocket = connSocket;
        this.index = index;
        this.server = server;
        this.outToClient = new DataOutputStream(connSocket.getOutputStream());
        this.inFromClient = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
    }

    public void run() {
        try {
            // Welcome player



            // Receive messages
            while (true) {
                System.out.println("Waiting for input");
                String input = inFromClient.readLine();
                System.out.println("Input received: " + input);

                String broadcastMessage = input + " " + index;
                server.broadcastMessage(broadcastMessage); // MOVE x y direction player_index
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String message) throws IOException { // MOVE x y direction player_index
        outToClient.writeBytes(message + '\n');
    }
}
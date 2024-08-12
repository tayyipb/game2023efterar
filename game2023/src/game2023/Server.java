package game2023;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static ArrayList<ServerThread> serverThreads = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        // Accept two players
        ServerSocket welcomeSocket = new ServerSocket(9876);

        for (int i = 0; i <= 3; i++) {
            Socket connectionSocket = welcomeSocket.accept();

            //Thread.sleep(3000);
            System.out.println("Bruger forbundet: " + connectionSocket.getRemoteSocketAddress());

            DataOutputStream outToOther = new DataOutputStream(connectionSocket.getOutputStream());
            String message = "Player " + i;
            outToOther.writeBytes(message + '\n');
            System.out.println("Player message sent");

            ServerThread serverThread = new ServerThread(connectionSocket, i, new Server());

            serverThreads.add(serverThread);
            serverThread.start();
            System.out.println("Server thread started");
        }

        while (true) {

        }
    }

    public synchronized void broadcastMessage(String message) {
        for (ServerThread thread : serverThreads) {
            try {
                thread.sendMessage(message); // MOVE x y direction player_index
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

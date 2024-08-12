package game2023;

import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class RecieveThread extends Thread {
	Socket connSocket;

	public RecieveThread(Socket connSocket) {
		this.connSocket = connSocket;
	}
	public void run() {
		try {
			BufferedReader inFromOther = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));

			while (true) {
				System.out.println("ReceiveThread waiting for input");
				String input = inFromOther.readLine();
				System.out.println("Input received: " + input); // MOVE x y direction player_index

				String[] parts = input.split(" ");

				if (parts.length >= 4 && parts[0].equalsIgnoreCase("MOVE")) {
					int x = Integer.parseInt(parts[1]);
					int y = Integer.parseInt(parts[2]);
					String direction = parts[3];
					int playerIndex = Integer.parseInt(parts[4]);

					Player player = GUI.getPlayer(playerIndex);


					Platform.runLater(() -> {
						try {
							GUI.updatePlayerMoved(player, x, y, direction);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					});
				} else {
					System.out.println("Invalid input format");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

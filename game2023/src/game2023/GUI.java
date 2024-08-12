package game2023;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.*;

public class GUI extends Application {

	public static final int height = 25;
	public static final int width = 41;
	public static final int size = 20; 
	public static final int scene_height = size * height + 100;
	public static final int scene_width = size * width + 200;

	public static Image image_bonfire;
	public static Image image_lava;
	public static Image image_portal;
	public static Image image_obstacle;
	public static Image image_floor;
	public static Image image_wall;
	public static Image hero_right,hero_left,hero_up,hero_down;
	public static Image player1_right,player1_left,player1_up,player1_down;
	public static Image player2_right,player2_left,player2_up,player2_down;
	public static Image player3_right,player3_left,player3_up,player3_down;
	public static Image player4_right,player4_left,player4_up,player4_down;

	public static Player player1;
	public static Player player2;
	public static Player player3;
	public static Player player4;
	public static Player me;
	public static List<Player> players = new ArrayList<>();

	private static Label[][] fields;
	private static TextArea scoreList;

	private static BufferedReader inFromUser;
	private static DataOutputStream outToOther;
	
	private static String[] board = {
			"wwwwwwwwwwwwwwwwwwwwpwwwwwwwwwwwwwwwwwwww",
			"w           llo           o  lll        w",
			"w                   o                   w",
			"wo                  o                   w",
			"wo            o           ol            w",
			"wo      wwwwwwwwwww   wwwwwwwwwww       w",
			"w      lw ll         lll        w       w",
			"w      lw                       w       w",
			"w      lw          ooo          wl    llw",
			"wwww  www           o           www  wwww",
			"w       w  o                 o  w       w",
			"w          o                 o          w",
			"p          ooo      b      ooo          p",
			"w          ol                o          w",
			"w       w  ol                o  w       w",
			"ww  wwwww           o           wwwww  ww",
			"w     oow          ooo          wll     w",
			"w       w                     llw       w",
			"w       wlll                  llw       w",
			"wl     lwwwwwwwwwww   wwwwwwwwwww      ow",
			"wl     ll     o           olll         ow",
			"wl                  o                   w",
			"w                   o                   w",
			"w             ol          o    ooo      w",
			"wwwwwwwwwwwwwwwwwwwwpwwwwwwwwwwwwwwwwwwww"
	};


	// -------------------------------------------
	// | Maze: (0,0)              | Score: (1,0) |
	// |-----------------------------------------|
	// | boardGrid (0,1)          | scorelist    |
	// |                          | (1,1)        |
	// -------------------------------------------

	@Override
	public void start(Stage primaryStage) {
		try {
			GridPane grid = new GridPane();
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(0, 10, 0, 10));

			Text mazeLabel = new Text("Maze:");
			mazeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

			Text scoreLabel = new Text("Score:");
			scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

			scoreList = new TextArea();
			
			GridPane boardGrid = new GridPane();

			image_bonfire = new Image(getClass().getResourceAsStream("Image/bonfire.png"),size,size,false,false);
			image_lava = new Image(getClass().getResourceAsStream("Image/lava.png"),size,size,false,false);
			image_wall  = new Image(getClass().getResourceAsStream("Image/coolwall.png"),size,size,false,false);
			image_floor = new Image(getClass().getResourceAsStream("Image/bedrefloor.png"),size,size,false,false);
			image_portal = new Image(getClass().getResourceAsStream("Image/portal.png"),size,size,false,false);
			image_obstacle = new Image(getClass().getResourceAsStream("Image/bush.png"),size,size,false,false);

			player1_right  = new Image(getClass().getResourceAsStream("Image/Player1Right.png"),size,size,false,false);
			player1_left  = new Image(getClass().getResourceAsStream("Image/Player1Left.png"),size,size,false,false);
			player1_up  = new Image(getClass().getResourceAsStream("Image/Player1Up.png"),size,size,false,false);
			player1_down  = new Image(getClass().getResourceAsStream("Image/Player1Down.png"),size,size,false,false);

			player2_right  = new Image(getClass().getResourceAsStream("Image/Player2Right.png"),size,size,false,false);
			player2_left  = new Image(getClass().getResourceAsStream("Image/Player2Left.png"),size,size,false,false);
			player2_up  = new Image(getClass().getResourceAsStream("Image/Player2Up.png"),size,size,false,false);
			player2_down  = new Image(getClass().getResourceAsStream("Image/Player2Down.png"),size,size,false,false);

			player3_right  = new Image(getClass().getResourceAsStream("Image/Player3Right.png"),size,size,false,false);
			player3_left  = new Image(getClass().getResourceAsStream("Image/Player3Left.png"),size,size,false,false);
			player3_up  = new Image(getClass().getResourceAsStream("Image/Player3Up.png"),size,size,false,false);
			player3_down  = new Image(getClass().getResourceAsStream("Image/Player3Down.png"),size,size,false,false);

			player4_right  = new Image(getClass().getResourceAsStream("Image/Player4Right.png"),size,size,false,false);
			player4_left  = new Image(getClass().getResourceAsStream("Image/Player4Left.png"),size,size,false,false);
			player4_up  = new Image(getClass().getResourceAsStream("Image/Player4Up.png"),size,size,false,false);
			player4_down  = new Image(getClass().getResourceAsStream("Image/Player4Down.png"),size,size,false,false);

			hero_right  = new Image(getClass().getResourceAsStream("Image/heroRight.png"),size,size,false,false);
			hero_left   = new Image(getClass().getResourceAsStream("Image/heroLeft.png"),size,size,false,false);
			hero_up     = new Image(getClass().getResourceAsStream("Image/heroUp.png"),size,size,false,false);
			hero_down   = new Image(getClass().getResourceAsStream("Image/heroDown.png"),size,size,false,false);

			fields = new Label[width][height];
			for (int j=0; j<height; j++) {
				for (int i=0; i<width; i++) {
					switch (board[j].charAt(i)) {
					case 'b':
						fields[i][j] = new Label("", new ImageView(image_bonfire));
						break;
					case 'l':
						fields[i][j] = new Label("", new ImageView(image_lava));
						break;
					case 'p':
						fields[i][j] = new Label("", new ImageView(image_portal));
						break;
					case 'o':
						fields[i][j] = new Label("", new ImageView(image_obstacle));
						break;
					case 'w':
						fields[i][j] = new Label("", new ImageView(image_wall));
						break;
					case ' ':					
						fields[i][j] = new Label("", new ImageView(image_floor));
						break;
					default: throw new Exception("Illegal field value: "+board[j].charAt(i) + i + " " + j);
					}
					boardGrid.add(fields[i][j], i, j);
				}
			}
			scoreList.setEditable(false);

			grid.add(mazeLabel,  0, 0); 
			grid.add(scoreLabel, 1, 0); 
			grid.add(boardGrid,  0, 1);
			grid.add(scoreList,  1, 1);
						
			Scene scene = new Scene(grid,scene_width,scene_height);
			primaryStage.setScene(scene);
			primaryStage.show();

			scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
				switch (event.getCode()) {
				case UP:
					try {
						playerMoved(0,-1,"up");
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					break;
				case DOWN:
					try {
						playerMoved(0,+1,"down");
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					break;
				case LEFT:
					try {
						playerMoved(-1,0,"left");
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					break;
				case RIGHT:
					try {
						playerMoved(+1,0,"right");
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					break;
				default: break;
				}
			});

			player1 = new Player("Caster",2,2,"up");
			players.add(player1);
			fields[2][2].setGraphic(new ImageView(player1_up));

			player2 = new Player("Archer",width - 3,height - 3,"up");
			players.add(player2);
			fields[width - 3][height - 3].setGraphic(new ImageView(player2_up));

			player3 = new Player("Wizard",2,height - 3,"up");
			players.add(player3);
			fields[2][height - 3].setGraphic(new ImageView(player3_up));

			player4 = new Player("Knight",width - 3,2,"up");
			players.add(player4);
			fields[width - 3][2].setGraphic(new ImageView(player4_up));
			scoreList.setText(getScoreList());

			connect();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void playerMoved(int delta_x, int delta_y, String direction) throws Exception {
		String message = "MOVE" + " " + delta_x + " " + delta_y + " " + direction;
		outToOther.writeBytes(message + "\n");
	}

	public static void updatePlayerMoved(Player player, int delta_x, int delta_y, String direction) throws Exception {
		player.direction = direction;
		int x = player.getXpos(),y = player.getYpos();

		if (board[y+delta_y].charAt(x+delta_x)=='b') {
			if (player.getPoint() < 0) {
				player.resetPoints();
				if (Math.random() < 0.10) {
					ArrayList<Player> playersCopy = new ArrayList<>(players);
					playersCopy.remove(player);

					playersCopy.forEach(p -> p.addPoints(100));
				}
			} else {
				player.addPoints(-100);
			}
		} else if (board[y+delta_y].charAt(x+delta_x)=='w') {
			player.addPoints(-5);
		} else if (board[y+delta_y].charAt(x+delta_x)=='o') {
			player.addPoints(-1);
		} else if (board[y+delta_y].charAt(x+delta_x)=='l') {
			player.addPoints(-200);
		} else if (board[y+delta_y].charAt(x+delta_x)=='p') {
			// TELEPORT
			if (x + delta_x == width/2 && y + delta_y == height - 1) {
				if (getPlayerAt(width / 2,1) == null) {
					fields[x][y].setGraphic(new ImageView(image_floor));

					x = width / 2;
					y = 1;

					player.setXpos(x);
					player.setYpos(y);

					if (player == players.get(0)) {
						fields[x][y].setGraphic(new ImageView(player1_down));
					} else if (player == players.get(1)) {
						fields[x][y].setGraphic(new ImageView(player2_down));
					} else if (player == players.get(2)) {
						fields[x][y].setGraphic(new ImageView(player3_down));
					} else if (player == players.get(3)) {
						fields[x][y].setGraphic(new ImageView(player4_down));
					} else {
						fields[x][y].setGraphic(new ImageView(hero_down));
					}
				}
			} else if (x + delta_x == width/2 && y + delta_y == 0) {
				if (getPlayerAt(width / 2, height - 2) == null) {
					fields[x][y].setGraphic(new ImageView(image_floor));

					x = width / 2;
					y = height - 2;

					player.setXpos(x);
					player.setYpos(y);

					if (player == players.get(0)) {
						fields[x][y].setGraphic(new ImageView(player1_up));
					} else if (player == players.get(1)) {
						fields[x][y].setGraphic(new ImageView(player2_up));
					} else if (player == players.get(2)) {
						fields[x][y].setGraphic(new ImageView(player3_up));
					} else if (player == players.get(3)) {
						fields[x][y].setGraphic(new ImageView(player4_up));
					} else {
						fields[x][y].setGraphic(new ImageView(hero_up));
					}
				}
			} else if (x + delta_x == 0 && y + delta_y == height / 2) {
				if (getPlayerAt(width - 2,height / 2) == null) {
					fields[x][y].setGraphic(new ImageView(image_floor));

					x = width - 2;
					y = height / 2;

					player.setXpos(x);
					player.setYpos(y);

					if (player == players.get(0)) {
						fields[x][y].setGraphic(new ImageView(player1_left));
					} else if (player == players.get(1)) {
						fields[x][y].setGraphic(new ImageView(player2_left));
					} else if (player == players.get(2)) {
						fields[x][y].setGraphic(new ImageView(player3_left));
					} else if (player == players.get(3)) {
						fields[x][y].setGraphic(new ImageView(player4_left));
					} else {
						fields[x][y].setGraphic(new ImageView(hero_left));
					}
				}
			} else if (x + delta_x == width - 1 && y + delta_y == height / 2) {
				if (getPlayerAt(1, height / 2) == null) {
					fields[x][y].setGraphic(new ImageView(image_floor));

					x = 1;
					y = height / 2;

					player.setXpos(x);
					player.setYpos(y);

					if (player == players.get(0)) {
						fields[x][y].setGraphic(new ImageView(player1_right));
					} else if (player == players.get(1)) {
						fields[x][y].setGraphic(new ImageView(player2_right));
					} else if (player == players.get(2)) {
						fields[x][y].setGraphic(new ImageView(player3_right));
					} else if (player == players.get(3)) {
						fields[x][y].setGraphic(new ImageView(player4_right));
					} else {
						fields[x][y].setGraphic(new ImageView(hero_right));
					}
				}
			}
			player.addPoints(-10);
		} else {
			Player p = getPlayerAt(x+delta_x,y+delta_y);
			if (p!=null) {
				player.addPoints(100);
				p.addPoints(-100);
			} else {
				player.addPoints(1);

				fields[x][y].setGraphic(new ImageView(image_floor));
				x+=delta_x;
				y+=delta_y;

				if (direction.equals("right")) {
					if (player == players.get(0)) {
						fields[x][y].setGraphic(new ImageView(player1_right));
					} else if (player == players.get(1)) {
						fields[x][y].setGraphic(new ImageView(player2_right));
					} else if (player == players.get(2)) {
						fields[x][y].setGraphic(new ImageView(player3_right));
					} else if (player == players.get(3)) {
						fields[x][y].setGraphic(new ImageView(player4_right));
					} else {
						fields[x][y].setGraphic(new ImageView(hero_right));
					}
				};
				if (direction.equals("left")) {
					if (player == players.get(0)) {
						fields[x][y].setGraphic(new ImageView(player1_left));
					} else if (player == players.get(1)) {
						fields[x][y].setGraphic(new ImageView(player2_left));
					} else if (player == players.get(2)) {
						fields[x][y].setGraphic(new ImageView(player3_left));
					} else if (player == players.get(3)) {
						fields[x][y].setGraphic(new ImageView(player4_left));
					} else {
						fields[x][y].setGraphic(new ImageView(hero_left));
					}
				};
				if (direction.equals("up")) {
					if (player == players.get(0)) {
						fields[x][y].setGraphic(new ImageView(player1_up));
					} else if (player == players.get(1)) {
						fields[x][y].setGraphic(new ImageView(player2_up));
					} else if (player == players.get(2)) {
						fields[x][y].setGraphic(new ImageView(player3_up));
					} else if (player == players.get(3)) {
						fields[x][y].setGraphic(new ImageView(player4_up));
					} else {
						fields[x][y].setGraphic(new ImageView(hero_up));
					}
				};
				if (direction.equals("down")) {
					if (player == players.get(0)) {
						fields[x][y].setGraphic(new ImageView(player1_down));
					} else if (player == players.get(1)) {
						fields[x][y].setGraphic(new ImageView(player2_down));
					} else if (player == players.get(2)) {
						fields[x][y].setGraphic(new ImageView(player3_down));
					} else if (player == players.get(3)) {
						fields[x][y].setGraphic(new ImageView(player4_down));
					} else {
						fields[x][y].setGraphic(new ImageView(hero_down));
					}
				};

				player.setXpos(x);
				player.setYpos(y);
			}
		}
		scoreList.setText(getScoreList());
	}

	public static String getScoreList() {
		StringBuffer b = new StringBuffer(100);
		for (Player p : players) {
			b.append(p+"\r\n");
		}
		return b.toString();
	}

	public static Player getPlayerAt(int x, int y) {
		for (Player p : players) {
			if (p.getXpos()==x && p.getYpos()==y) {
				return p;
			}
		}
		return null;
	}

	public static Player getPlayer(int index) {
		return players.get(index);
	}

	public static void setPlayer(int index) {
		me = players.get(index);
	}

	public static void connect() throws Exception {
		String ip = "10.10.132.200";
		Socket TCPSocket = new Socket(ip, 9876);

		inFromUser = new BufferedReader(new InputStreamReader(TCPSocket.getInputStream()));
		outToOther = new DataOutputStream(TCPSocket.getOutputStream());

		String messageReceived = inFromUser.readLine();
		System.out.println(messageReceived);
		String[] words = messageReceived.split(" ");

		if (words.length > 1 && "Player".equals(words[0])) {
			int playerIndex = Integer.parseInt(words[1]);

			GUI.setPlayer(playerIndex);
		}

		(new RecieveThread(TCPSocket)).start();
		System.out.println("Connect Call finished");
	}

}


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package football.Game;

import football.Game.Actions.Action;
import Physics.Circle;
import Physics.World;
import TCPServerClient.Connection;
import TCPServerClient.ConnectionCallback;
import football.LobbyServer.LobbyPlayer;
import football.LobbyServer.Room;
import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nicknacck
 */
public class Game extends Thread implements ConnectionCallback, Drawable {

    private boolean destroy = false;
    private final World world = new World(FIELDWIDTH, FIELDHEIGHT);
    private final ArrayList<Action> actions= new ArrayList<>();

    List<Player> players = Collections.synchronizedList(new ArrayList<>());
    List<Message> messages = Collections.synchronizedList(new ArrayList<>());

    ArrayList<Player> teamRed = new ArrayList<>();
    ArrayList<Player> teamBlue = new ArrayList<>();

    private boolean run = true;

    private static final int FIELDHEIGHT = 500;
    private static final int FIELDWIDTH = 1000;
    private static final int BALLRADIUS = 10;
    private static final int PLAYERRADIUS = 15;

    private static final int GAMEPLAYERLINK = 1;

    public Game(Room room) throws IOException {
        world.circles.add(new Circle(FIELDWIDTH / 2, FIELDHEIGHT / 2, 0, 0, BALLRADIUS));
        int ppt = room.getPlayerPerTeam();
        double y = 500 / (ppt + 1);

        for (int i = 0; i < room.getTeamRed().size(); i++) {
            Circle circle = new Circle(50, y * (i + 1), 0, 0, PLAYERRADIUS);
            LobbyPlayer lobbbyplayer = room.getTeamRed().get(i);
            Player player = new Player(lobbbyplayer.getID(), lobbbyplayer.name);
            lobbbyplayer.con.setLink(GAMEPLAYERLINK, player);
            player.setCircle(circle);
            teamRed.add(player);
            world.circles.add(circle);
        }
        for (int i = 0; i < room.getTeamBlue().size(); i++) {
            Circle circle = new Circle(FIELDWIDTH - 50, y * (i + 1), 0, 0, PLAYERRADIUS);
            LobbyPlayer lobbbyplayer = room.getTeamBlue().get(i);
            Player player = new Player(lobbbyplayer.getID(), lobbbyplayer.name);
            lobbbyplayer.con.setLink(GAMEPLAYERLINK, player);
            player.setCircle(circle);
            teamBlue.add(player);
            world.circles.add(circle);
        }

    }

    public Game() {
        world.circles.add(new Circle(FIELDWIDTH / 2, FIELDHEIGHT / 2, 0, 0, BALLRADIUS));
        int ppt = 1;
        double y = 500 / (ppt + 1);
        Circle circle1 = new Circle(50, y * (0 + 1), 10, 0.00000001d, PLAYERRADIUS);
        Circle circle2 = new Circle(FIELDWIDTH - 50, y * (0 + 1), 0, 0, PLAYERRADIUS);
        Player player1 = new Player(0, "ha");
        Player player2 = new Player(0, "lo");
        player1.setCircle(circle1);
        player2.setCircle(circle2);
        teamRed.add(player1);
        teamBlue.add(player2);
        world.circles.add(circle1);
        world.circles.add(circle2);
        
    }

    public void joinGame(Room room) {
        for (int i = 0; i < room.getTeamRed().size(); i++) {
            LobbyPlayer lobbbyplayer = room.getTeamRed().get(i);
            lobbbyplayer.con.addCallback(this);
            lobbbyplayer.send("JOINGAME");
            lobbbyplayer.game = this;
        }
        for (int i = 0; i < room.getTeamBlue().size(); i++) {
            LobbyPlayer lobbbyplayer = room.getTeamBlue().get(i);
            lobbbyplayer.con.addCallback(this);
            lobbbyplayer.send("JOINGAME");
            lobbbyplayer.game = this;
        }
    }

    @Override
    public void run() {

        while (run) {
            processMessages();
            world.step();
            try {
                synchronized (this) {
                    wait(20);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void processMessages() {
        while (!messages.isEmpty()) {
            try {
                Message msg = messages.get(0);
                messages.remove(0);
                String[] split = msg.msg.split("]");
                switch (split[0]) {
                    case "": {
                        break;
                    }
//                case "":{
//                    break;
//                }
                }
            } catch (Exception e) {
            }

        }
    }

    public boolean shouldDestroy() {
        return destroy;
    }

    @Override
    public void newMessage(Connection con, String msg) {
        messages.add(new Message((Player) con.getLink(GAMEPLAYERLINK), msg));
    }

    @Override
    public void lostConnection(Connection con) {
    }

    @Override
    public void render(Graphics g) {
        world.render(g);
    }

    private static class Message {

        Player player;
        String msg;

        public Message(Player player, String msg) {
            this.player = player;
            this.msg = msg;
        }
    }
}

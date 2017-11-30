/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package football.Game;

import Physics.Circle;
import Physics.World;
import TCPServerClient.Connection;
import TCPServerClient.ConnectionCallback;
import TCPServerClient.ServerCallback;
import TCPServerClient.TCPServer;
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
    private World world;

    List<Player> players = Collections.synchronizedList(new ArrayList<>());
    List<Message> messages = Collections.synchronizedList(new ArrayList<>());

    ArrayList<Player> teamRed = new ArrayList<>();
    ArrayList<Player> teamBlue = new ArrayList<>();
    private boolean run;

    private static final int FIELDHEIGHT = 500;
    private static final int FIELDWIDTH = 1000;
    private static final int BALLRADIUS = 10;
    private static final int PLAYERRADIUS = 15;
    
    private static final int GAMEPLAYERLINK = 1;
    
    public Game(Room room) throws IOException {
        world= new World(FIELDWIDTH, FIELDHEIGHT);
        world.circles.add(new Circle(FIELDWIDTH/2, FIELDHEIGHT/2, 0, 0, BALLRADIUS));
        int ppt = room.getPlayerPerTeam();
        double y=500/(ppt+1);
        
        for (int i = 0; i < room.getTeamRed().size(); i++) {
            Circle circle = new Circle(50, y*(i+1), 0, 0, PLAYERRADIUS);
            LobbyPlayer lobbbyplayer = room.getTeamRed().get(i);
            Player player = new Player(lobbbyplayer.getID(),lobbbyplayer.name);
            lobbbyplayer.con.setLink(GAMEPLAYERLINK, player);
            player.setCircle(circle);
            teamRed.add(player);
            world.circles.add(circle);
        }
        for (int i = 0; i < room.getTeamBlue().size(); i++) {
            Circle circle = new Circle(FIELDWIDTH-50, y*(i+1), 0, 0, PLAYERRADIUS);
            LobbyPlayer lobbbyplayer = room.getTeamBlue().get(i);
            Player player = new Player(lobbbyplayer.getID(),lobbbyplayer.name);
            lobbbyplayer.con.setLink(GAMEPLAYERLINK, player);
            player.setCircle(circle);
            teamBlue.add(player);
            world.circles.add(circle);
        }
        
    }
    
    public void joinGame(Room room){
        for (int i = 0; i < room.getTeamRed().size(); i++) {
            LobbyPlayer lobbbyplayer = room.getTeamRed().get(i);
            lobbbyplayer.con.addCallback(this);
            lobbbyplayer.send("JOINGAME");
            lobbbyplayer.game= this;
        }
        for (int i = 0; i < room.getTeamBlue().size(); i++) {
            LobbyPlayer lobbbyplayer = room.getTeamBlue().get(i);
            lobbbyplayer.con.addCallback(this);
            lobbbyplayer.send("JOINGAME");
            lobbbyplayer.game= this;
        }
    }

    @Override
    public void run() {
        
        while (run) {
            processMessages();
            try {
                Thread.sleep(20);
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
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.red);
        for (int i = 0; i < circles.size(); i++) {
            Circle get = circles.get(i);
            g.setColor(Color.red);
            g.fillOval((int) (get.pos.x - get.radius), (int) (get.pos.y - get.radius), (int) (get.radius * 2), (int) (get.radius * 2));
            g.setColor(Color.BLUE);
            g.drawLine((int) (get.pos.x), (int) (get.pos.y), (int) (get.pos.x + get.speed.x * 10), (int) (get.pos.y + get.speed.y * 10));
        }
        
        for (int i = 0; i < debugLines.size(); i++) {
            World.Line get = debugLines.get(i);
            g.setColor(get.color);
            g.drawLine(get.x, get.y, get.x2, get.y2);
        }

        String forces = "";
        double all = 0;
        for (int i = 0; i < circles.size(); i++) {
            double length = circles.get(i).speed.length();
            try {
                forces += "c1: " + (length + "").substring(0, 4) + " + ";
            } catch (Exception e) {
            }

            all += length;
        }
        forces += "= " + all;
        g.setColor(Color.WHITE);
        g.drawString(forces, 20, 20);
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

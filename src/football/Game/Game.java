/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package football.Game;

import TCPServerClient.Connection;
import TCPServerClient.ServerCallback;
import TCPServerClient.TCPServer;
import football.LobbyServer.Room;
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
public class Game extends Thread implements ServerCallback {
    private final int port;
    private final TCPServer server;
    private boolean destroy = false;

    List<Player> players = Collections.synchronizedList(new ArrayList<>());
    List<Message> messages = Collections.synchronizedList(new ArrayList<>());

    ArrayList<Player> teamRed = new ArrayList<>();
    ArrayList<Player> teamBlue = new ArrayList<>();
    private boolean run;

    public Game(int port, Room room) throws IOException {
        this.port = port;
        for (int i = 0; i < room.getTeamRed().size(); i++) {
            Player player = new Player(room.getTeamRed().get(i).getID());
            teamRed.add(player);
        }
        for (int i = 0; i < room.getTeamBlue().size(); i++) {
            Player player = new Player(room.getTeamBlue().get(i).getID());
            teamBlue.add(player);
        }
        server = new TCPServer(port, this, room.getPlayerPerTeam() * 2);
        
    }

    @Override
    public void run() {
        server.start();
        
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

    public Integer getPort() {
        return port;
    }

    @Override
    public void newConnection(Connection con) {
    }

    @Override
    public void newMessage(Connection con, String msg) {
        messages.add(new Message(con, msg));
    }

    @Override
    public void lostConnection(Connection con) {
    }

    private static class Message {

        Connection con;
        String msg;

        public Message(Connection con, String msg) {
            this.con = con;
            this.msg = msg;
        }

    }
}

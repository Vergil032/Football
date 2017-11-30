/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package football.LobbyServer;

import TCPServerClient.Connection;
import TCPServerClient.ConnectionCallback;
import TCPServerClient.ServerCallback;
import TCPServerClient.TCPServer;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 *
 * @author nicknacck
 */
public class LobbyServer implements ServerCallback {

    private final HashMap<Long, LobbyPlayer> players = new HashMap<>();
    private final HashMap<String, Room> rooms = new HashMap<>();
    private final List<Message> messages = Collections.synchronizedList(new ArrayList<>());
    private final TCPServer server;
    private boolean run = true;
    public static final String NEWCONMSG = "NEWCONNECTION";
    public static final String LOSTCONMSG = "LOSTCONNECTION";

    public static final int PORT = 1234;

    public LobbyServer(int port) throws IOException {
        server = new TCPServer(PORT, this);
    }

    @Override
    public void newConnection(Connection con) {
        //racecondition?????
        long nextID;
        do {
            nextID = new Random().nextLong();
        } while (players.get(nextID) != null);

        LobbyPlayer newPlayer = new LobbyPlayer(nextID, con);
        players.put(nextID, newPlayer);
        con.link = newPlayer;
    }

    @Override
    public void newMessage(Connection con, String msg) {
        messages.add(new Message((LobbyPlayer) con.link, msg));
    }

    @Override
    public void lostConnection(Connection con) {
        newMessage(con, LOSTCONMSG);
    }

    public void start() throws InterruptedException {
        server.start();
        while (run) {
            if (!messages.isEmpty()) {
                Message msg = messages.get(0);
                processMessage(msg);
                messages.remove(0);
            }
            Thread.sleep(20);
        }
    }

    private void processMessage(Message msg) {

        if (msg == null || msg.message == null || msg.player == null) {
            return;
        }

        System.out.println("From " + msg.player.name + ": " + msg.message);

        String[] split = msg.message.split("]");

        try {
            switch (split[0]) {
                case LOSTCONMSG: {
                    players.remove(msg.player.getID());
                    leaveRoom(msg.player);
                    break;
                }

                case "PULL": {
                    msg.player.send(roomsToJSON());
                    break;
                }
                case "JOIN": {
                    String pass = split[1];
                    String roomName = split[2];
                    Room room = rooms.get(roomName);
                    joinRoom(msg.player, room, pass);
                    break;
                }
                case "CREATE": {
                    String name = split[1];
                    String pass = split[2];
                    int ppt = Integer.valueOf(split[3]);
                    int wincon = Integer.valueOf(split[4]);
                    int winval = Integer.valueOf(split[5]);

                    if (rooms.get(name) != null) {
                        return;
                    }

                    if (msg.player.getRoom() != null) {
                        return;
                    }

                    Room room = new Room(name, ppt, wincon, winval, msg.player, this, pass);
                    rooms.put(name, room);
                    joinRoom(msg.player, room, pass);

                    break;
                }
                case "CHANGE": {
                    Room r = msg.player.getRoom();
                    if (r == null) {
                        return;
                    }
                    r.changeTeam(msg.player);

                    break;
                }
                case "NAME": {
                    msg.player.name = split[1];
                    break;
                }
                case "LEAVE": {
                    Room room = msg.player.getRoom();
                    if (room == null) {
                        return;
                    }
                    leaveRoom(msg.player);

                    break;
                }
                case "START": {
                    Room room = msg.player.getRoom();
                    if (room.getAdmin() != msg.player || !room.canStart()) {
                        return;
                    }
                    GameHoster.newGame(room);
                    destroyRoom(room);
                    break;
                }

                //case "": {
//                    break;
//                }
                default: {
                    break;
                }
            }
        } catch (Exception e) {
        }

    }

    public void joinRoom(LobbyPlayer player, Room room, String pass) {
        if (player.getRoom() != null) {
            return;
        }

        int er = room.join(player, pass);
        if (er != -1) {
            player.setRoom(room);
        }

    }

    public void leaveRoom(LobbyPlayer p) {
        if (p.getRoom() != null) {
            p.getRoom().leave(p);
            p.setRoom(null);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        LobbyServer lobbyServer = new LobbyServer(1234);
        lobbyServer.start();
    }

    private String roomsToJSON() {
        String json = "{\"lobby\":[";
        Iterator<Room> iterator = rooms.values().iterator();
        while (iterator.hasNext()) {
            Room next = iterator.next();
            json += roomToJSON(next);
            json += ",";
        }
        if (!rooms.isEmpty()) {
            json = json.substring(0, json.length() - 1);
        }
        json += "]}";
        return json;
    }

    private String roomToJSON(Room room) {
        return "{\"room\":{\"name\":\"" + room.getName() + "\", \"wintype\":\""
                + room.getWincondition() + "\", \"winvalue\":\"" + room.getWinvalue() + "\", \"maxPlayer\": \""
                + room.getPlayerPerTeam() * 2 + "\", \"actualPlayer\":\"" + room.getAmountOfPlayer() + "\",\"pass\":\""
                + (room.getPassword().equals("") ? "no" : "yes") + "\"}}";
    }

    public void destroyRoom(Room room) {
        room.leaveAll();
        rooms.remove(room.getName());
    }

    class Message {

        LobbyPlayer player;
        String message;

        public Message(LobbyPlayer player, String message) {
            this.player = player;
            this.message = message;
        }

    }

}

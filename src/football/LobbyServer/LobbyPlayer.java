/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package football.LobbyServer;

import TCPServerClient.Connection;
import football.Game.Game;

/**
 *
 * @author nicknacck
 */
public class LobbyPlayer {

    private final long ID;
    public String name;
    private Room room;
    public final Connection con;
    public Game game;

    public LobbyPlayer(long ID, Connection con) {
        this.ID = ID;
        this.con = con;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    public long getID() {
        return ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void send(String msg) {
        if (con != null) {
            con.send(msg);
        }
    }

}

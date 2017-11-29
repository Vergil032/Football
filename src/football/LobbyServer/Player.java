/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package football.LobbyServer;

import TCPServerClient.Connection;

/**
 *
 * @author nicknacck
 */
public class Player {

    private final long ID;
    public String name;
    private Room room;
    private final Connection con;
    
    public Player(long ID, Connection con) {
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
    
    public void send(String msg){
        con.send(msg);
    }
    
    
    
}

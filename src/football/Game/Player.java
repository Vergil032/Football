/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package football.Game;

import TCPServerClient.Connection;
import Physics.Circle;
/**
 *
 * @author nicknacck
 */
public class Player {
    private long id;
    private Circle circle;
    private String name;
    public Player(long id, String name) {
        this.id = id;
        this.name=name;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public Circle getCircle() {
        return circle;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }
    
    
}

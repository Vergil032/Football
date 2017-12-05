/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package football.Game.Actions;

import Physics.Circle;
import Util.Timer;
import Vector.Vector2d;
import football.Game.Player;

/**
 *
 * @author nicknacck
 */
public class Dash extends Action {

    Player player;
    Vector2d point;
    Timer t = new Timer(1000);

    public Dash(Player player, Vector2d point) {
        this.player = player;
        this.point = point;
    }

    @Override
    public void execute() {
        if (t.isTime()) {
            
            point= new Vector2d(Math.random()*500, Math.random()*500);
            t.reset();
            Circle c = player.getCircle();
            c.speed = point.minus(c.pos).getNormal().factor(40);
        }
    }

}

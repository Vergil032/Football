/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package football.Game.Actions;

import Physics.Circle;
import Vector.Vector2d;
import football.Game.Player;

/**
 *
 * @author nicknacck
 */
public class Drag extends Action{
    
    private final Player player;
    private final Vector2d point;

    public Drag(Player player, Vector2d point) {
        this.player = player;
        this.point = point;
    }
    
    @Override
    public void execute() {
        Circle circle = player.getCircle();
        
        circle.speed=circle.speed.plus(circle.pos.minus(point).invert().getNormal().factor(0.2));
    }
    
}

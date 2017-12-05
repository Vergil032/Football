/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package football.Game.Actions;

import Physics.Circle;
import Vector.Vector2d;

/**
 *
 * @author nicknacck
 */
public class Gravity extends Action{
    
    Circle circle;
    
    public Gravity(Circle circle) {
        this.circle=circle;
    }
    
    
    @Override
    public void execute() {
        circle.speed=circle.speed.plus(new Vector2d(0, 0.1));
    }
    
}

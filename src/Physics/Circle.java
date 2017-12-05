/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics;

import Vector.Vector2d;

/**
 *
 * @author nicknacck
 */
public class Circle {
    public Vector2d pos,speed;
    double radius;
    private double friction=0.95;
    
    public Circle(double x, double y, double sx, double sy, double radius) {
        pos= new Vector2d(x, y);
        speed= new Vector2d(sx, sy);
        this.radius = radius;
    }
    
    void update() {
        speed=speed.factor(friction);
        pos=pos.plus(speed);
    }

    public boolean collide(Circle b) {
        return World.getLength(b.pos.x, b.pos.y, pos.x, pos.y) < (radius + b.radius);
    }
    
    
}

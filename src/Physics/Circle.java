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
    Vector2d pos,speed;
    double radius;
    double friction=0.9999;
    
    public Circle(double x, double y, double sx, double sy, double radius) {
        pos= new Vector2d(x, y);
        speed= new Vector2d(sx, sy);
        this.radius = radius;
    }
    
    void update() {
        pos.x+=speed.x;
        pos.y+=speed.y;
    }

    public boolean collide(Circle b) {
        return World.getLength(b.pos.x, b.pos.y, pos.x, pos.y) < (radius + b.radius);
    }
    
    
}

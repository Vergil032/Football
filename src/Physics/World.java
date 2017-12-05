/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics;

import Vector.Vector2d;
import football.Game.Actions.Action;
import football.Game.Drawable;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
/**
 *
 * @author nicknacck
 */
public class World implements Drawable{

    public ArrayList<Circle> circles = new ArrayList<>();
    private final int borderX, borderY;
    private int step=0;
    public final ArrayList<Action> actions= new ArrayList<>();
    private final ArrayList<Line> debugLines = new ArrayList<>();
    
    public World(int borderX, int borderY) {
        this.borderX = borderX;
        this.borderY = borderY;
    }
    
    public int stepTo(int stepTo){
        int oldStep=step;
        int toGo=stepTo-step;
        for (int i = 0; i < toGo; i++) {
            step();
            actions();
        }
        return oldStep;
    }
    
    public int step() {
        step++;
        stepPositions();
        checkBorders();
        checkCollisions();
        return step;
    }

    private void stepPositions() {
        for (int i = 0; i < circles.size(); i++) {
            Circle get = circles.get(i);
            get.update();
        }
    }

    private void checkCollisions() {

        ArrayList<Collision> collsions = new ArrayList<>();
        for (int i = 0; i < circles.size(); i++) {
            for (int j = i + 1; j < circles.size(); j++) {
                Circle a = circles.get(i);
                Circle b = circles.get(j);
                if (a.collide(b)) {
                    collsions.add(new Collision(a, b));
                }
            }
        }
        for (int i = 0; i < collsions.size(); i++) {
            Collision get = collsions.get(i);
            collisionCalc(get);
        }
    }

    private void collisionCalc(Collision col) {
        Circle a = col.a;
        Circle b = col.b;

        Vector2d ab = a.pos.minus(b.pos).getNormal();
        Vector2d abOrth = ab.getOrthoVector().getNormal();
        double backa = Math.sin(abOrth.angle(a.speed));
        double backb = Math.sin(abOrth.angle(b.speed));
        
        double speedAB=a.speed.length()+b.speed.length();
        Vector2d pointCol=a.pos.minus(ab.factor(a.radius));
        debugLines.add(new Line((int)(pointCol.x), (int)(pointCol.y),(int)(pointCol.x+abOrth.factor(20).x), (int)(pointCol.y+abOrth.factor(20).y), Color.yellow));
        
        if (Double.isNaN(backa)) {
            backa = 0;
        }
        if (Double.isNaN(backb)) {
            backb = 0;
        }
        Vector2d factorA = ab.factor(backa * a.speed.length());
        Vector2d factorB = ab.invert().factor(backb * b.speed.length());
        Vector2d of=factorA.minus(factorB);
        debugLines.add(new Line((int)(pointCol.x), (int)(pointCol.y),(int)(pointCol.x+of.factor(20).x), (int)(pointCol.y+of.factor(20).y), Color.LIGHT_GRAY));
        
//        if (factorA.length() < factorB.length()) {
//            of = factorA.minus(factorB);
//        } else {
//            of = factorB.minus(factorA);
//        }
        a.speed = a.speed.plus(of);
        b.speed = b.speed.minus(of);
        
        double speedAB2=a.speed.length()+b.speed.length();
        double correction=speedAB/speedAB2;
        a.speed = a.speed.factor(correction);
        b.speed = b.speed.factor(correction);
//        a.speed = a.speed.minus(factorA);
//        b.speed = b.speed.plus(factorB);

//        System.out.println("ab: " + ab);
//        System.out.println("abOrth: " + abOrth);
//        System.out.println("backa: " + backa);
//        System.out.println("backb: " + backb);
//        System.out.println("of: " + of);
//        System.out.println("");
    }

    public static double getLength(double x1, double y1) {
        return Math.sqrt(Math.pow(x1, 2) + Math.pow(y1, 2));
    }

    public static double getLength(double x1, double y1, double x2, double y2) {
        double distX = x1 - x2;
        double distY = y1 - y2;

        return Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
    }

    private void checkBorders() {
        for (int i = 0; i < circles.size(); i++) {
            Circle get = circles.get(i);
            Vector2d pos = get.pos;
            Vector2d speed = get.speed;
            if (pos.x - get.radius < 0) {
                pos.x = get.radius;
                speed.x *= -1;
            }
            if (pos.y - get.radius < 0) {
                pos.y = get.radius;
                speed.y *= -1;
            }
            if (pos.x + get.radius > borderX) {
                pos.x = borderX - get.radius;
                speed.x *= -1;
            }
            if (pos.y + get.radius > borderY) {
                pos.y = borderY - get.radius;
                speed.y *= -1;
            }
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, borderX, borderY);
        g.setColor(Color.red);
        for (int i = 0; i < circles.size(); i++) {
            Circle get = circles.get(i);
            g.setColor(Color.red);
            g.fillOval((int) (get.pos.x - get.radius), (int) (get.pos.y - get.radius), (int) (get.radius * 2), (int) (get.radius * 2));
            g.setColor(Color.BLUE);
            g.drawLine((int) (get.pos.x), (int) (get.pos.y), (int) (get.pos.x + get.speed.x * 10), (int) (get.pos.y + get.speed.y * 10));
        }
        
        for (int i = 0; i < debugLines.size(); i++) {
            World.Line get = debugLines.get(i);
            g.setColor(get.color);
            g.drawLine(get.x, get.y, get.x2, get.y2);
        }

        String forces = "";
        double all = 0;
        for (int i = 0; i < circles.size(); i++) {
            double length = circles.get(i).speed.length();
            try {
                forces += "c1: " + (length + "").substring(0, 4) + " + ";
            } catch (Exception e) {
            }

            all += length;
        }
        forces += "= " + all;
        g.setColor(Color.WHITE);
        g.drawString(forces, 20, 20);
        
        g.drawString("Step: "+step, 20, 40);
    }

    private void actions() {
        for (int i = 0; i < actions.size(); i++) {
            Action get = actions.get(i);
            get.execute();
        }
    }

    private static class Line {
        private int x,y,x2,y2;
        private Color color;

        public Line(int x, int y, int x2, int y2, Color color) {
            this.x = x;
            this.y = y;
            this.x2 = x2;
            this.y2 = y2;
            this.color=color;
        }
        
    }

    class Collision {

        Circle a, b;

        public Collision(Circle a, Circle b) {
            this.a = a;
            this.b = b;
        }

    }
}

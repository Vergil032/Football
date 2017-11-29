/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics;

import Vector.Vector2d;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;

/**
 *
 * @author nicknacck
 */
public class World extends JPanel {

    private ArrayList<Circle> circles = new ArrayList<>();
    private int borderX = 1000, borderY = 500;

    public World() {
        Random random = new Random(1337);
//        for (int i = 0; i < 10; i++) {
//            Circle circle = new Circle(Math.abs(random.nextInt() % 1000),Math.abs(random.nextInt() % 500), random.nextDouble()% 30, random.nextDouble()% 30, Math.abs(random.nextInt() % 50));
//            circles.add(circle);
//        }
        circles.add(new Circle(00, 00, 1, 1, 50));
        //circles.add(new Circle(200, 100, 0, 0, 20));
        circles.add(new Circle(500, 00, -1, 1, 50));

//        circles.add(new Circle(420, 300, 0, 1, 50));
//        //circles.add(new Circle(200, 100, 0, 0, 20));
//        circles.add(new Circle(350, 400, 0, 0, 50));
        System.out.println("");
    }

    public void step() {
        stepPositions();
        checkBorders();
        checkCollisions();
    }

    private void stepPositions() {
        for (int i = 0; i < circles.size(); i++) {
            Circle get = circles.get(i);
            get.update();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.red);
        for (int i = 0; i < circles.size(); i++) {
            Circle get = circles.get(i);
            g.setColor(Color.red);
            g.fillOval((int) (get.pos.x - get.radius), (int) (get.pos.y - get.radius), (int) (get.radius * 2), (int) (get.radius * 2));
            g.setColor(Color.BLUE);
            g.drawLine((int) (get.pos.x), (int) (get.pos.y), (int) (get.pos.x + get.speed.x * 10), (int) (get.pos.y + get.speed.y * 10));
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
            collisionCalc(get, 1);
        }
    }

    private void collisionCalc(Collision col, double bounceOf) {
        Circle a = col.a;
        Circle b = col.b;

        Vector2d ab = a.pos.minus(b.pos).getNormal();
        Vector2d abOrth = ab.getOrthoVector().getNormal();
        double backa = Math.sin(abOrth.angle(a.speed));
        double backb = Math.sin(abOrth.invert().angle(b.speed));
        if (Double.isNaN(backa)) {
            backa = 0;
        }
        if (Double.isNaN(backb)) {
            backb = 0;
        }
        Vector2d factorA = ab.factor(backa * a.speed.length());
        Vector2d factorB = ab.factor(backb * b.speed.length());
        Vector2d of;
        if(factorA.length()<factorB.length()){
            of=factorA.minus(factorB);
        }else{
            of=factorB.minus(factorA);
        }
        a.speed = a.speed.minus(of);
        b.speed = b.speed.plus(of);
//        a.speed = a.speed.minus(factorA);
//        b.speed = b.speed.plus(factorB);
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

    class Collision {

        Circle a, b;

        public Collision(Circle a, Circle b) {
            this.a = a;
            this.b = b;
        }

    }
}

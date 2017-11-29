/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Vector;

/**
 *
 * @author User
 */
public class Vector2d {
    
    public double x,y;
    
    protected Vector2d(){}
    
    public Vector2d(double x, double y){
        this.x=x;
        this.y=y;
    }
    
    public double length(){
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }
    
    public static Vector2d getRandomVector(int low, int high) { //full circle 0-360
        double r = random(low, high);
        double radians = Math.toRadians(r);
        return new Vector2d(Math.cos(radians), Math.sin(radians));
    }

    public Vector2d invert() {
        return new Vector2d(-x, -y);
    }

    public static double random(double low, double high) {
        return Math.random() * (high - low) + low;
    }

    public static int randomInt(int low, int high) {
        return (int) Math.round(Math.random() * (high - low) + low);
    }

    public static Vector2d getVector(double sPx, double sPy, double ePx, double ePy) {
        return new Vector2d(ePx - sPx, ePy - sPy);
    }
    
    public Vector2d minus(Vector2d v){
        return new Vector2d(x-v.x, y-v.y);
    }
    
    public Vector2d plus(Vector2d v){
        return new Vector2d(x+v.x, y+v.y);
    }
    
    public Vector2d factor(double f){
        return new Vector2d(x*f, y*f);
    }
    
    public Vector2d mirror(double rad){
        double cos2=Math.cos(2*rad);
        double sin2=Math.sin(2*rad);
        return new Vector2d(x*cos2+y*sin2, x*sin2-y*cos2);
    }
    
    public Vector2d mirror(Vector2d v){
        return mirror(v.angle());
    }
    
    public static void main(String[] args) {
        Vector2d v = new Vector2d(10, 10);
        System.out.println(Math.toDegrees(v.angle()));
    }

    @Override
    public String toString() {
        return "x: "+ x + " y: "+y;
    }
    
    
//
//    public static Point2d getPointOfIntersection(Line line1, Line line2) {
//
//        Point2d a = new Point2d(line1.posX, line1.posY);
//        Point2d b = new Point2d(line2.posX, line2.posY);
//
//        Vector2d g = new Vector2d(line1.x, line1.y);
//        Vector2d h = new Vector2d(line2.x, line2.y);
//
//        double t = (a.x + (b.y - a.y) / g.y * g.x - b.x) / (h.x - h.y / g.y * g.x);
//
//        return new Point2d(b.x + h.x * t, b.y + h.y * t);
//    }
    
    public double scalar(Vector2d v1){
        return v1.x*x+v1.y*y;
    }
    
    public double angle(){
        return Math.acos(x/length());
    }
    
    public double angle(Vector2d v){
        double v1len = v.length();
        double v2len = length();
        if(v1len!=0 && v2len!=0){
            return Math.acos(scalar(v)/(v1len*v2len));
        }else{
            return Double.NaN;
        }
    }
    
    public Vector2d getOrthoVector(){
        if(y==0){
            return new Vector2d(0,1);
        }
        return new Vector2d(1, (-x/y));
    }
    
    public Vector2d getNormal(){
        double length = length();
        if(length==0){
            return new Vector2d();
        }
        return new Vector2d(x/length, y/length);
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics;

import javax.swing.JFrame;

/**
 *
 * @author nicknacck
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        JFrame jFrame = new JFrame("testStuff");
        World world = new World();
        jFrame.add(world);
        jFrame.setSize(1200, 700);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
        
        while(true){
            world.step();
            jFrame.repaint();
            Thread.sleep(5);
            //(long) (Math.random()*100)
        }
        
    }
    
    
    
}

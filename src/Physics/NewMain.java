/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Physics;

import TCPServerClient.Connection;
import football.Game.Debugjpanl;
import football.Game.Game;
import football.LobbyServer.LobbyPlayer;
import football.LobbyServer.Room;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author nicknacck
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException, IOException {
        JFrame jFrame = new JFrame("testStuff");
        Game game=new Game();
        game.start();
        JPanel world = new Debugjpanl(game);
        jFrame.add(world);
        jFrame.setSize(1200, 700);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
        
        while(true){
            
            jFrame.repaint();
            Thread.sleep(20);
            //(long) (Math.random()*100)
        }
        
    }
    
    
    
}

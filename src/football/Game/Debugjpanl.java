/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package football.Game;

import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author nicknacck
 */
public class Debugjpanl extends JPanel{
    
    Drawable d;

    public Debugjpanl(Drawable d) {
        this.d = d;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        d.render(g);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package football.Game.Actions;

import football.Game.Player;
import java.awt.Point;

/**
 *
 * @author nicknacck
 */
public class Drag extends Action{
    
    private Player player;
    private Point point;

    public Drag(Player player, Point point) {
        this.player = player;
        this.point = point;
    }
    
    @Override
    public void execute() {
        player.getCircle().
    }
    
}

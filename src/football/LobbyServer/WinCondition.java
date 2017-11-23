/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package football.LobbyServer;

/**
 *
 * @author nicknacck
 */
public class WinCondition {
    int value;
    int type;
    
    public static final int SCORE=0;
    public static final int TIME=1;
    
    public WinCondition(int value, int type) {
        this.value = value;
        this.type = type;
    }
    
}

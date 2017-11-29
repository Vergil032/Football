/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package football.LobbyServer;

import football.Game.Game;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author nicknacck
 */
public class GameHoster {
    private static final HashMap<Integer, Game> games= new HashMap<>();
    
    
    public static void clearGames(){
        
    }

    static void newGame(Room room) throws IOException {
        int port= newPort();
        Game game= new Game(port, room);
        games.put(port, game);
        game.start();
        room.start(port);
        
    }

    private static int newPort() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

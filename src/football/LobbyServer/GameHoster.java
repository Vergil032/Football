/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package football.LobbyServer;

import football.Game.Game;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author nicknacck
 */
public class GameHoster {

    private static final ArrayList games = new ArrayList();

    public static void clearGames() {

    }

    static void newGame(Room room) throws IOException {
        if (room.canStart()) {
            Game game = new Game(room);
            games.add(game);
            game.joinGame(room);
            room.start();
            game.start();
        }
    }

}

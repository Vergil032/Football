/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package football.GameServer;

import TCPServerClient.Connection;
import TCPServerClient.ServerCallback;
import TCPServerClient.TCPServer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author nicknacck
 */
public class GameServer implements ServerCallback {

    private final TCPServer server;
    private final HashMap<Integer, Game> games = new HashMap<>();
    List<Game> newGame = Collections.synchronizedList(new ArrayList<>());
    
    private static int PORT = 123;
    private boolean run;

    public GameServer(int port) throws IOException {
        server = new TCPServer(port, this, 1);
        
    }

    public void start() throws InterruptedException {
        server.start();
        
        while(run){
            checkNewGame();
            checkFinishedGames();
            Thread.sleep(20);
        }
        
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        GameServer gameServer = new GameServer(PORT);
        gameServer.start();
    }

    @Override
    public void newConnection(Connection con) {
    }

    @Override
    public void newMessage(Connection con, String msg) {
        try {
            String[] split = msg.split("]");
            switch (split[0]) {
                case "NEWGAME": {
                    int port;
                    int ppt=0; //split
                    do {
                        port = (int) (Math.random() * 65535);
                    } while (games.get(port) != null || port==PORT);

//                    Game game = new Game(port,ppt);
//                    newGame.add(game);
                    
                    con.send("NEWGAMEPORT]" + port);
                    break;
                }
//                case "":{
//                    break;
//                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void lostConnection(Connection con) {
    }
    
    private void checkNewGame(){
        if (!newGame.isEmpty()) {
            int size = newGame.size();
            for (int i = 0; i < size; i++) {
                Game get = newGame.get(i);
                games.put(get.getPort(), get);
            }
            for (int i = 0; i < size; i++) {
                newGame.remove(0);
            }
        }
    }
    
    private void checkFinishedGames(){
        Iterator<Game> iterator = games.values().iterator();
        ArrayList<Integer> rem= new ArrayList<>();
        
        while(iterator.hasNext()){
            Game next = iterator.next();
            if(next.shouldDestroy()){
                rem.add(next.getPort());
            }
        }
        
        for (int i = 0; i < rem.size(); i++) {
            games.remove(rem.get(i));
        }
    }

}

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

/**
 *
 * @author nicknacck
 */
public class GameServer implements ServerCallback{
    
    private final TCPServer server;
    private final ArrayList<Game> games= new ArrayList<>();
    
    private static int PORT = 123;
    
    public GameServer(int port) throws IOException{
        server = new TCPServer(port, this);
    }
    
    public void start(){
        server.start();
    }
    
    public static void main(String[] args) throws IOException {
        GameServer gameServer = new GameServer(PORT);
        gameServer.start();
    }

    @Override
    public void newConnection(Connection con) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void newMessage(Connection con, String msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void lostConnection(Connection con) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

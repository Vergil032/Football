/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCPServerClient;

import TCPServerClient.Connection;
import TCPServerClient.ConnectionCallback;
import TCPServerClient.ServerCallback;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nicknacck
 */
public class TCPServer extends Thread implements ConnectionCallback{

    private final ServerSocket serverSocket;
    private boolean run=true;
    public final static Logger LOGGER = Logger.getLogger(TCPServer.class.getName());
    private final ServerCallback callback;
    private final int maxConnetion;
    private int conCounter = 0;

    public TCPServer(int port, ServerCallback callback) throws IOException {
        this(port, callback, -1);
    }

    public TCPServer(int port, ServerCallback callback, int maxConnetion) throws IOException {
        serverSocket = new ServerSocket(port);
        LOGGER.log(Level.INFO, "server started");
        this.callback = callback;
        this.maxConnetion = maxConnetion;
    }

    @Override
    public void run() {
        while (run) {
            try {
                Socket socket = serverSocket.accept();
                if (conCounter < maxConnetion) {
                    conCounter++;
                    Connection con = new Connection(socket, this);
                    con.start();
                    callback.newConnection(con);
                    LOGGER.log(Level.INFO, "new connection: {0}", socket);
                }else{
                    socket.close();
                }
            } catch (IOException err) {
                err.printStackTrace();
            }
        }
    }

    
    public void stopServer() throws IOException{
        serverSocket.close();
        run=false;
    }

    @Override
    public void newMessage(Connection con, String msg) {
        callback.newMessage(con, msg);
    }

    @Override
    public void lostConnection(Connection con) {
        LOGGER.log(Level.INFO, "lost connection: {0}", con);
        callback.lostConnection(con);
    }
    
    
}

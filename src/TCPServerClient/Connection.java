/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCPServerClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author nicknacck
 */
public class Connection extends Thread {

    private final Socket socket;
    public final InputStream inputStream;
    public final OutputStream outputStream;
    private ConnectionCallback callback;
    public Object link;

    public Connection(Socket socket, ConnectionCallback callback) throws IOException {
        this.socket = socket;
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        this.callback = callback;
    }

    public void setCallback(ConnectionCallback callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        try {
            int read;
            while ((read = inputStream.read(buffer, 0, 1024)) != -1) {
                if (callback != null) {
                    callback.newMessage(this, new String(buffer, 0, read));
                }
            }
        } catch (IOException ex) {
        }
        close();
        if (callback != null) {
            callback.lostConnection(this);
        }
    }

    public void send(String msg) {
        if (msg == null) {
            return;
        }
        try {
            outputStream.write((msg).getBytes());
        } catch (IOException ex) {
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException ex) {
        }
    }

    @Override
    public String toString() {
        return socket.toString();
    }

}

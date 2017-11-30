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
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author nicknacck
 */
public class Connection extends Thread {

    private final Socket socket;
    public final InputStream inputStream;
    public final OutputStream outputStream;
    private final ArrayList<ConnectionCallback> callback = new ArrayList<>();
    private final HashMap<Integer, Object> linker = new HashMap<>();

    public Connection(Socket socket, ConnectionCallback callback) throws IOException {
        this.socket = socket;
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        this.callback.add(callback);
    }

    public void addCallback(ConnectionCallback callback) {
        this.callback.add(callback);
    }

    public void removeCallback(ConnectionCallback callback) {
        this.callback.remove(callback);
    }

    public void setLink(int link, Object o) {
        linker.put(link, o);
    }

    public Object getLink(int link) {
        return linker.get(link);
    }

    public void removeLink(int link) {
        linker.remove(link);
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        try {
            int read;
            while ((read = inputStream.read(buffer, 0, 1024)) != -1) {
                for (int i = 0; i < callback.size(); i++) {
                    ConnectionCallback get = callback.get(i);
                    if (get != null) {
                        get.newMessage(this, new String(buffer, 0, read));
                    }
                }

            }
        } catch (IOException ex) {
        }
        close();
        for (int i = 0; i < callback.size(); i++) {
            ConnectionCallback get = callback.get(i);
            if (get != null) {
                get.lostConnection(this);
            }
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

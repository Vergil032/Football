/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TCPServerClient;

import TCPServerClient.Connection;

/**
 *
 * @author nicknacck
 */
public interface ConnectionCallback {
    public abstract void newMessage(Connection con, String msg);
    public abstract void lostConnection(Connection con);
}

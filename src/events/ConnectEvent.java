/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package events;

import java.net.Socket;
import java.util.EventObject;

/**
 *
 * @author Junior Javier
 */
public class ConnectEvent extends EventObject {
    
    Socket sc;

    public ConnectEvent(Socket o, Object source) {
        super(source);
        sc = o;
    }
    
    public Socket getSocket(){
        return sc;
    }
    public void setSocket(Socket socket) {
        this.sc = socket;
    }
    
}

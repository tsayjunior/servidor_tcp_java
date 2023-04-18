/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import events.ConnectEvent;

import events.SocketEventListenner;
import javax.swing.event.EventListenerList;
/**
 *
 * @author Junior Javier
 */
public class threadConnections extends Thread  {
    
    Socket socket;
    ServerSocket servidor;
    
    public threadConnections(ServerSocket servidor) {
        this.servidor = servidor;
    }
    
     // ----------------- eventos ----------------------------
    protected EventListenerList listenerList = new EventListenerList();

    public void addMyEventListener(SocketEventListenner listener) {
        listenerList.add(SocketEventListenner.class, listener);
    }

    public void removeMyEventListener(SocketEventListenner listener) {
        listenerList.remove(SocketEventListenner.class, listener);
    }

    void fireMyEvent(ConnectEvent evt) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == SocketEventListenner.class) {
                ((SocketEventListenner) listeners[i + 1]).onConnected(evt);
            }
        }
    }
    
    
    // fin eventos
    
    @Override
    public void run() {

        try {
            System.out.println("entra a run en cliente Conexiones ");
            while (true) {
                socket = servidor.accept();
                
                ConnectEvent ce = new ConnectEvent(socket);
                fireMyEvent(ce);
            }
        } catch (IOException ex) {
            Logger.getLogger(threadConnections.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import events.DataEvent;
import events.SocketEventListenner;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.EventListenerList;

/**
 *
 * @author Junior Javier
 */
public class threadMessage extends Thread {

    DataInputStream in;
    DataOutputStream out;
    Socket socket;

    public threadMessage(Socket sc) {
        socket = sc;
    }

    @Override
    public void run() {
        while (true) {

            try {
                BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
                BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
                while (true) {
                    System.out.println("entra a run de threadMessage");
//                    in = new DataInputStream(socket.getInputStream());
                    byte[] buffer = new byte[1024];
                    int bytesLeidos = in.read(buffer);
                    String respuesta = new String(buffer, 0, bytesLeidos);
                    System.out.println("Respuesta del Cliente: " + respuesta);
//                    String mensaje = in.readUTF();
                    DataEvent ce = new DataEvent(respuesta);
                    fireMyEvent(ce);
                }
            } catch (IOException ex) {
                Logger.getLogger(threadMessage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * ************************** eventos *****************************
     */
    protected EventListenerList listenerList = new EventListenerList();

    public void addMyEventListener(SocketEventListenner listener) {
        listenerList.add(SocketEventListenner.class, listener);
    }

    public void removeMyEventListener(SocketEventListenner listener) {
        listenerList.remove(SocketEventListenner.class, listener);
    }

    void fireMyEvent(DataEvent evt) {
        System.out.println("entra a fireMyEvent");
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == SocketEventListenner.class) {
                ((SocketEventListenner) listeners[i + 1]).onReader(evt);
            }
        }
    }

    /**
     * ************ fin eventos ********************
     */
}
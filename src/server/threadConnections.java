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
import java.net.SocketException;
import javax.swing.event.EventListenerList;
/**
 *
 * @author Junior Javier
 */
public class threadConnections extends Thread  {
    
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
        conexion();
    }
    public void conexion() {
        // Aceptar conexiones de clientes
        Socket socket;
        while (true) {
            try {
                // Aceptar conexiones de clientes
//                System.out.println("entra a conexion threadConnections");
                socket = servidor.accept();
//                System.out.println("entra a conexion threadConnections fireMyEvent");
                ConnectEvent evento = new ConnectEvent(socket, this);
                this.fireMyEvent(evento);
                //System.out.println("Después del evento en HiloConexion");
            } catch (SocketException e) {
                // Manejo de la excepción SocketException
                System.out.println("Se ha cerrado la conexión del cliente de manera abrupta (threadConnections).");
                System.out.println(e);
                // Puedes cerrar los recursos y finalizar el hilo de manera adecuada aquí
            } catch (IOException e) {
                System.err.println("Servidor: Error en la conexión con el cliente: " + e.getMessage());
            }
        }
    }
}

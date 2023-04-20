/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import events.DataEvent;
import events.SocketEventListenner;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.EventListenerList;
import javax.swing.event.EventListenerList;

/**
 *
 * @author Junior Javier
 */
public class threadMessage extends Thread {

    Socket socket;
    private boolean clienteDesconectado = false;

    protected EventListenerList listenerList = new EventListenerList();
    
    public threadMessage(Socket socket) {
        this.socket = socket;
    }

    public void setClienteDesconectado(boolean clienteDesconectado) {
        this.clienteDesconectado = clienteDesconectado;
    }

    @Override
    public void run() {
        // Obtener los flujos de entrada y salida del socket
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);

            String mensaje;
            while ((mensaje = entrada.readLine()) != null) {
                //System.out.println("Mensaje recibido del cliente: " + mensaje);
                //  salida.println("Respuesta del servidor: " + mensaje.toUpperCase());       
                DataEvent evento = new DataEvent(mensaje);
                this.fireMyEvent(evento);
            }

            // Cierra los flujos y el socket cuando se termina la comunicación con el cliente
            entrada.close();
            socket.close();
        } catch (SocketException e) {
            // Manejo de la excepción SocketException
            System.out.println("Se ha cerrado la conexión del cliente de manera abrupta (threadMessage).");
            // Puedes cerrar los recursos y finalizar el hilo de manera adecuada aquí
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Cerrar el hilo cuando el cliente se desconecta
            System.out.println("Cerrando el hilo Mensaje del servidor con el cliente...");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //****************PARA LLAMAR A LOS EVENTOS

    public void addMyEventListener(SocketEventListenner listener) {
        listenerList.add(SocketEventListenner.class, listener);
    }

    public void removeMyEventListener(SocketEventListenner listener) {
        listenerList.remove(SocketEventListenner.class, listener);
    }

    void fireMyEvent(DataEvent evt) {
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

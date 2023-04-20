/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import events.DesconectionEvent;
import events.SocketEventListenner;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import javax.swing.event.EventListenerList;

/**
 *
 * @author Junior Javier
 */
public class threadPingCustomer extends Thread {

    private Map<String, Map<String, Object>> Clientes;
    protected EventListenerList listenerList = new EventListenerList();

    public threadPingCustomer(Map<String, Map<String, Object>> clientes) {
        this.Clientes = clientes;
    }

    @Override
    public void run() {
        System.out.println("entra a threadPingCustomer");
        while (true) {
            // Verificar el estado de cada cliente en la lista
                System.out.println("entra a while en threadPingCustomer");
            for (Map.Entry<String, Map<String, Object>> entry : Clientes.entrySet()) {
                String key = entry.getKey(); // Obtener la clave del mapa exterior
                Map<String, Object> valor = entry.getValue();
                Socket socket = (Socket) valor.get("socket");
                if (socket.isClosed() || !socket.isConnected()) {
                    DesconectionEvent evento = new DesconectionEvent(key);
                    NotificarEvento(evento);//notificar evento desconexion al servidor
                    break; // Salir del bucle para evitar ConcurrentModificationException
                }
            }

            try {
                // Esperar 5 segundos antes de la siguiente verificación
                Thread.sleep(5000);
                System.out.println("entra a esperar 5 segundos");
            } catch (InterruptedException e) {
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

    void NotificarEvento(DesconectionEvent evt) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == SocketEventListenner.class) {
                ((SocketEventListenner) listeners[i + 1]).onDesconection(evt);
            }
        }
    }
}

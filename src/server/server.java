/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import events.ConnectEvent;
import events.DataEvent;
import events.SocketEventListenner;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Junior Javier
 */
public class server implements SocketEventListenner {

    public Map<Integer, Socket> customer = new HashMap<Integer, Socket>();

    public void correrServidor(int puerto) {
        ServerSocket servidor;

        try {
            servidor = new ServerSocket(puerto);
            System.out.println("**********servidor iniciado*************");

            threadConnections conexiones = new threadConnections(servidor);
            conexiones.addMyEventListener(this);
            conexiones.start();

        } catch (IOException ex) {
            Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void onConnected(ConnectEvent evt) {
        System.out.println("entra a onConnected, aqui debe añadir al hasmap");
        int aux = customer.size() + 1;
        customer.put(aux, evt.sendSocket());
        System.out.println("aux " + aux);

        // Crear un String con los datos que deseas enviar
        String mensaje = "Tu identificador es : " + aux;
        // Convertir el String en un arreglo de bytes utilizando la codificación UTF-8
        byte[] buffer = mensaje.getBytes(StandardCharsets.UTF_8);

        threadMessage tm = new threadMessage(evt.sendSocket());
        tm.addMyEventListener(this);
        tm.start();
    }

    @Override
    public void onReader(DataEvent evt) {
        System.out.println("entra a onReader " + evt.sendMessage());

        // Crear un String con los datos que deseas enviar
        String mensaje = "mensaje de respuesta del servidor";
        // Convertir el String en un arreglo de bytes utilizando la codificación UTF-8
        byte[] buffer = mensaje.getBytes(StandardCharsets.UTF_8);
        for (Socket socket : customer.values()) {
            try {
                System.out.println(customer.values());
                System.out.println(socket);
                OutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());

                System.out.println("Valor: " + socket);
//                outputStream.write(buffer);
//                outputStream.flush(); // asegura que todos los datos se envíen
//                outputStream.write(buffer);
            } catch (IOException e) {
                // manejar excepción
            }
        }
    }
}

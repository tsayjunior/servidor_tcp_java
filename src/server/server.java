/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import customer.customer;
import events.ConnectEvent;
import events.DataEvent;
import events.SocketEventListenner;
import java.io.BufferedInputStream;
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

        OutputStream outputStream;
        BufferedInputStream in;
        try {
            outputStream = new BufferedOutputStream(evt.sendSocket().getOutputStream());
            in = new BufferedInputStream(evt.sendSocket().getInputStream());
            // Crear un String con los datos que deseas enviar
            send_message_buffer("Tu identificador es : " + aux, outputStream);
            String respuesta = recive_message_buffer(in);
            send_message_buffer("introduce tu nick ", outputStream);
            respuesta = recive_message_buffer(in);
            
        } catch (IOException ex) {
            Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex);
        }

        threadMessage tm = new threadMessage(evt.sendSocket());
        tm.addMyEventListener(this);
        tm.start();
    }

    @Override
    public void onReader(DataEvent evt) {
        System.out.println("entra a onReader " + evt.sendMessage());        
        System.out.println("entra a onReader " + evt.sendMessage().getMessage());


        // Crear un String con los datos que deseas enviar
        String mensaje = "mensaje de respuesta del servidor";
        // Convertir el String en un arreglo de bytes utilizando la codificación UTF-8
//        byte[] buffer = mensaje.getBytes(StandardCharsets.UTF_8);
//        for (Socket socket : customer.values()) {
//            try {
//                System.out.println(customer.values());
//                System.out.println(socket);
//                OutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());
//
//                System.out.println("Valor: " + socket);
//                outputStream.write(buffer);
//                outputStream.flush(); // asegura que todos los datos se envíen
//                outputStream.write(buffer);
//            } catch (IOException e) {
//                // manejar excepción
//            }
//        }
    }

    /**
     * ** ***** funciones *************
     */
    public void send_message_buffer(String message, OutputStream outputStream) {
        // Convertir el String en un arreglo de bytes utilizando la codificación UTF-8
        byte[] buffer = message.getBytes(StandardCharsets.UTF_8);
        try {
            outputStream.write(buffer);
            outputStream.flush(); // asegura que todos los datos se envíen
        } catch (IOException ex) {
            Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String recive_message_buffer(BufferedInputStream in) {

        byte[] buffer = new byte[1024];
        String respuesta = null;
        int bytesLeidos;
        try {
            bytesLeidos = in.read(buffer);
            respuesta = new String(buffer, 0, bytesLeidos);
        } catch (IOException ex) {
            Logger.getLogger(customer.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(respuesta);
        return respuesta;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import events.ConnectEvent;
import events.DataEvent;
import events.DesconectionEvent;
import events.SocketEventListenner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Junior Javier
 */
public class server implements SocketEventListenner {

    private Map<String, Map<String, Object>> customer = new HashMap<>();
    int port;//puerto
    ServerSocket server;

    public server(int port) {
        this.port = port;
    }

    public void correrServidor() {
        try {
            // Crear un socket de servidor en el puerto especificado
            server = new ServerSocket(this.port);
            System.out.println("**********servidor iniciado : esperando conexiones *************");
            threadPingCustomer hpc = new threadPingCustomer(customer);
            hpc.addMyEventListener(this);
            hpc.start();
//            System.out.println("luego de ir a threadPingCustomer");
            threadConnections conexiones = new threadConnections(server);
            conexiones.addMyEventListener(this);
            conexiones.start();
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Método para generar un identificador único para el cliente
    private String generarClienteId() {
        return UUID.randomUUID().toString(); // Utilizar la clase UUID para generar un identificador único
    }

    //Agregar cliente a la lista con HashMap
    public void agregarCliente(String clienteId, String nombre, Socket socket) {
        Map<String, Object> atributosCliente = new HashMap<>(); // HashMap para almacenar los atributos del cliente
        atributosCliente.put("nombre", nombre);
        atributosCliente.put("socket", socket);
        customer.put(clienteId, atributosCliente); // Agregar el cliente al mapa usando el identificador único como clave
    }

    @Override
    public void onConnected(ConnectEvent evt) {
//        System.out.println("entra a onConnected");
        Socket socket = evt.getSocket();
        try {
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida.println(generarClienteId());//Envía el ID al cliente
//**********solicita nombre al cliente
            salida.println("Por favor, ingrese su nombre:");
            String key = in.readLine();//Espera que el cliente mande su nombre
//********agregar cliente a la lista hashmap
            String[] parte = key.split(",");//El cliente manda el ID,Nombre, separar en un vector
            salida.println("Servidor : Conexión exitosa");
            System.out.println("se CONECTÓ el cliente con id: " + parte[0] + " y nombre: " + parte[1]);
            agregarCliente(parte[0], parte[1], socket);//ID,Nombre, Socket
            System.out.println("******************************************************");
            if (customer.size() == 0) {
                System.out.println("¡NINGÚN CLIENTE ESTÁ CONECTADO!");
            } else {
                System.out.println("TIENES " + customer.size() + "CLIENTES CONECTADOS");
            }
//]*******Avisar que se unio un nuevo cliente
            EnviarMensajes("Se unió " + parte[1] + " a la conversación", parte[0]);//se unió un nuevo cliente
//********Inicializar Hilo mensaje
            threadMessage hm = new threadMessage(socket);//En donde vamos a leer lo que el cliente nos envía, también vamos a enviar
            //una salida al Cliente
            hm.addMyEventListener(this);
            hm.start();
        } catch (SocketException e) {
            // Manejo de la excepción SocketException
            System.out.println("Se ha cerrado la conexión del cliente de manera abrupta (server).");
            // Puedes cerrar los recursos y finalizar el hilo de manera adecuada aquí
        } catch (IOException ex) {
            Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onReader(DataEvent evt) {
        String parte[] = evt.getMensaje().split(",");
        Map<String, Object> cliente = customer.get(parte[0]);
        System.out.println("mensaje nuevo de <" + parte[0] + ", " + cliente.get("nombre") + "> : " + parte[1]);
        String mensaje = (String) cliente.get("nombre") + ": " + parte[1];
        //  System.out.println("mensaje: "+mensaje);
        EnviarMensajes(mensaje, parte[0]);
    }

    @Override
    public void onDesconection(DesconectionEvent evt) {

        Map<String, Object> cliente = customer.remove(evt.getKey());
        String mensaje = (String) cliente.get("nombre") + " abandono la conversación";
        EnviarMensajes(mensaje, evt.getKey());
        System.out.println("Se DESCONECTÓ el cliente con id: " + evt.getKey() + " y nombre" + cliente.get("nombre"));
        System.out.println("**************************************************************************");
        if (customer.size() == 0) {
            System.out.println("¡NINGÚN CLIENTE ESTÁ CONECTADO!");
        } else {
            System.out.println("TIENES " + customer.size() + " CONECTADOS");
        }
    }

    /**
     * ** ***** funciones *************
     */

    public void EnviarMensajes(String mensaje, String clienteId) {
        for (Map.Entry<String, Map<String, Object>> entry : customer.entrySet()) {
            String key = entry.getKey(); // Obtener la clave del mapa exterior
            if (key.equals(clienteId) != true) {
                // System.out.println(entry);
                Map<String, Object> valor = entry.getValue();
                Socket socket = (Socket) valor.get("socket");
                try {
                    PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
                    salida.println(mensaje);
                } catch (IOException ex) {
                    Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}

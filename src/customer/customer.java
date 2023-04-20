/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.server;

/**
 *
 * @author Junior Javier
 */
public class customer {

    public String id; // Identificador único del cliente
    private static boolean isDisconnected = false; // Bandera que indica si el cliente está desconectado

    
    // ... otras propiedades y métodos de la clase Cliente ...
    // Métodos getter y setter para el identificador único del cliente
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void initCliente() {
        try {
            Socket socket = new Socket("localhost", 5000); // Conecta al servidor en el puerto 8080

            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
//*************Para obtener el cliente Id del servidor
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//Buffer de entrada
            String clienteId = in.readLine();//Espera que el servidor le mande el mensaje inicial
            System.out.println("cliente Id: " + clienteId);
            this.setId(clienteId);//Se guarda el id que le pasa el servidor al cliente
//*************Para Enviar el nombre del cliente

            System.out.println(in.readLine());
            String nombreCliente = teclado.readLine();
            salida.println(this.id + "," + nombreCliente); // Enviar el nombre del cliente al servidor
//*************Respuesta del servidor
            System.out.println(in.readLine());
//**********Iniciar la escucha de mensajes
            threadListenMessageServer hems = new threadListenMessageServer(socket);
            hems.start();

            String mensajeCliente;
            System.out.println("Escriba 'salir' para abandonar la conversación");
            System.out.println("Escriba un Nuevo mensaje");
            System.out.println("**********************************************");
            //   System.out.println("Yo:");
            String mensaje = "Yo: ";
            while ((mensajeCliente = teclado.readLine()) != null) {
                if ("salir".equalsIgnoreCase(mensajeCliente)) {
                    break; // Salir del ciclo si el usuario ingresa 'salir'
                }
                System.out.println(mensaje + mensajeCliente);
                salida.println(this.id + "," + mensajeCliente); // Enviar mensajes del usuario (cliente) al servidor
                // System.out.println("Yo:");

            }
            // Cierra los flujos y el socket cuando se termina la comunicación con el servidor
            // Cerrar la conexión con el servidor
            salida.close();
            socket.close();
        } catch (SocketException e) {
            // Capturar la excepción SocketException
            System.err.println("Se ha perdido la conexión con el servidor: " + e.getMessage());
            // Acciones específicas para manejar la desconexión, como cerrar la conexión o reconectar
            // ...
        } catch (IOException e) {
            if (e.getMessage().equals("Socket closed")) {
                System.out.println("socket cerrado");
                // El socket ya ha sido cerrado previamente, no es necesario tomar ninguna acción
            } else {
                e.printStackTrace();
            }
        } finally {
            isDisconnected = true; // Actualizar la bandera indicando que el cliente se ha desconectado
        }
    }

    public static boolean isDisconnected() {
        return isDisconnected;
    }

}

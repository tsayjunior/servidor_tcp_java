/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Junior Javier
 */
public class threadListenMessageServer extends Thread {
    
    Socket socket;
    BufferedReader in;

    public threadListenMessageServer(Socket socket) {
        this.socket = socket;

    }

    @Override
    public void run() {

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String mensajeServidor;
            while (!customer.isDisconnected()) {
                mensajeServidor = in.readLine();
                System.out.println(mensajeServidor); // Mostrar mensajes del servidor en la consola
            }

        } catch (SocketException e) {//ERROR CUANDO EL SOCKET SE CIERRA INESPERADAMENTE
            // Capturar la excepción SocketException
            System.err.println("Se ha perdido la conexión con el servidor: " + e.getMessage());
            // Acciones específicas para manejar la desconexión, como cerrar la conexión o reconectar
            // ...
        } catch (IOException e) {
            if (e.getMessage().equals("Socket closed")) {
                // El socket ya ha sido cerrado previamente, no es necesario tomar ninguna acción
            } else {
                e.printStackTrace();
            }
        } finally {
            System.out.println("Cliente desconectado desde el hilo escucha mensaje, cerrando el hilo...");
            // Cerrar el socket del cliente cuando el hilo se cierra
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

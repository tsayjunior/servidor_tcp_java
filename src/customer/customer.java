/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Junior Javier
 */
public class customer {

    private Socket sc;

    public customer(int puerto) {
        try {
            this.sc = new Socket("127.0.0.1", puerto);
            System.out.println("entra a constructor cliente");
        } catch (IOException ex) {
            Logger.getLogger(customer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void initCliente() {
        try {
//            in = new DataInputStream(sc.getInputStream());
//            out = new DataOutputStream(sc.getOutputStream());
            System.out.println("entra a init cliente");

            BufferedOutputStream out = new BufferedOutputStream(sc.getOutputStream());
            BufferedInputStream in = new BufferedInputStream(sc.getInputStream());

            while (true) {

                //envio mensaje del cliente
//                out.writeUTF("Hola mundo desde el cliente !!!");
                String mensaje = "Hola desde cliente!";
                out.write(mensaje.getBytes());
                out.flush(); // Asegura que todos los datos se envíen
                //Recibo el mensaje del servidor
                System.out.println("Antes de recibir mensaje");
                
                byte[] buffer = new byte[1024];
                int bytesLeidos = in.read(buffer);
                String respuesta = new String(buffer, 0, bytesLeidos);
                System.out.println("Respuesta del servidor: " + respuesta);
//                String mensaje = in.readUTF();
                System.out.println("despues de recibir mensaje");
//            System.out.println(mensaje);
            }
        } catch (IOException ex) {
            Logger.getLogger(customer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

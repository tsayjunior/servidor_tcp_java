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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.server;

/**
 *
 * @author Junior Javier
 */
public class customer {

    private Socket sc;
    private int id;
    private String name;

    public customer(int puerto) {
        try {
            this.sc = new Socket("127.0.0.1", puerto);
            System.out.println("entra a constructor cliente");
        } catch (IOException ex) {
            Logger.getLogger(customer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void initCliente() {
        try {
            System.out.println("entra a init cliente");
            BufferedOutputStream out = new BufferedOutputStream(sc.getOutputStream());
            BufferedInputStream in = new BufferedInputStream(sc.getInputStream());

            String respuesta = recive_message_buffer(in);
            this.id = get_id(respuesta);
            send_message_buffer("identificador recibido", out);
            respuesta = recive_message_buffer(in);
            send_message_buffer(getName(), out);

            pakage_message msg = new pakage_message(getId(), getName(), "");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(sc.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(sc.getInputStream());
            while (true) {
                msg.setMessage("Holde desde cliente y siendo objeto");

                objectOutputStream.writeObject(msg);

                objectOutputStream.flush();

//                String mensaje = "Hola desde cliente!";
//                out.write(mensaje.getBytes());
//                out.flush(); // Asegura que todos los datos se envíen
                //Recibo el mensaje del servidor
                System.out.println("Antes de recibir mensaje");

//                Person person = (Person) objectInputStream.readObject();
                System.out.println("despues de recibir mensaje");
            }
        } catch (IOException ex) {
            Logger.getLogger(customer.class.getName()).log(Level.SEVERE, null, ex);
        }

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

    public int get_id(String value) {
        int num = Integer.parseInt(value.substring(value.lastIndexOf(" ") + 1));
        return (num); // Imprime: 123
    }
}

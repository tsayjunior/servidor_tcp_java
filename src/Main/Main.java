/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;
import server.server;
/**
 *
 * @author Junior Javier
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        server s = new server(5000);
         s.correrServidor();
         
    }
    
}

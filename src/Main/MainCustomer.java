/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import customer.customer;

/**
 *
 * @author Junior Javier
 */
public class MainCustomer {

    public static void main(String[] args) {

        customer custom = new customer(5000);
        custom.setName("Javier");
        custom.initCliente();
        

//        customer c2 = new customer(5000);
//        c2.initCliente();
    }
}

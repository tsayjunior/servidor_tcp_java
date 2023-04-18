/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package events;

import java.util.EventObject;
import customer.pakage_message;
/**
 *
 * @author Junior Javier
 */
public class DataEvent  extends EventObject {
    public static pakage_message pack;


    public DataEvent(pakage_message o) {
        super(o);
        pack = o;
    }
    public pakage_message sendMessage(){
        return pack;
    }
}

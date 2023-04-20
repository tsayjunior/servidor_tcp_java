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
    
    String message;

    public DataEvent(String o) {
        super(o);
        this.message = o;
    }
    
    public String getMensaje() {
        return message;
    }

    public void setMensaje(String mensaje) {
        this.message = mensaje;
    }
}

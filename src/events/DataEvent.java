/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package events;

import java.util.EventObject;

/**
 *
 * @author Junior Javier
 */
public class DataEvent  extends EventObject {
    public static String data;

    public DataEvent(String o) {
        super(o);
        data = o;
    }
    public String sendMessage(){
        return data;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package events;

import java.util.EventListener;

/**
 *
 * @author Junior Javier
 */
public interface SocketEventListenner extends EventListener{
    
  public void onConnected(ConnectEvent evt);
  public void onReader(DataEvent evt);

}

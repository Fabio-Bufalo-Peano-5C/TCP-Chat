/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servertcp;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author fabio
 */
public class ListaThreadOnline implements Runnable {
    
    GUIServer GUI;
    
    public ListaThreadOnline(GUIServer GUI){
        this.GUI = GUI;
    }

    @Override
    public void run() {
        try {
            while(!Thread.interrupted()){
                String msg = "";
                for(int i = 0; i < GUI.clientList.size(); i++){
                    msg = msg+" "+ GUI.clientList.get(i);
                }
                
                for(int i = 0; i < GUI.socketList.size(); i++){
                    Socket tsoc = (Socket) GUI.socketList.get(i);
                    DataOutputStream dos = new DataOutputStream(tsoc.getOutputStream());
                    if(msg.length() > 0){
                        dos.writeUTF("CMD_ONLINE "+ msg);
                    }
                }
                
                Thread.sleep(1900);
            }
        } catch(InterruptedException ex){
            GUI.appendMessage("[InterruptedException]: "+ ex.getMessage());
        } catch (IOException e) {
            GUI.appendMessage("[IOException]: "+ e.getMessage());
        }
    }
    
    
}

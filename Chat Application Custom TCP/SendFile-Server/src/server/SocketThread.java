/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servertcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 *
 * @author fabio
 */
public class SocketThread implements Runnable{
    
    Socket socket;
    GUIServer GUI;
    DataInputStream dis;
    StringTokenizer st;
    String client;
    
    public SocketThread(Socket socket, GUIServer GUI){
        this.GUI = GUI;
        this.socket = socket;
        
        try {
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
            GUI.appendMessage("[SocketThreadIOException]: "+ ex.getMessage());
        }
    }
    
    @Override
    public void run() {
        try {
            while(true){
                String data = dis.readUTF();
                st = new StringTokenizer(data);
                String CMD = st.nextToken();
                switch(CMD){
                    case "CMD_JOIN":
                        String clientUsername = st.nextToken();
                        client = clientUsername;
                        GUI.setClientList(clientUsername);
                        GUI.setSocketList(socket);
                        GUI.appendMessage("[Client]: "+ clientUsername +" e entrato nella chat!");
                        break;
                        
                    case "CMD_CHAT":
                        String da = st.nextToken();
                        String a = st.nextToken();
                        String msg = "";
                        while(st.hasMoreTokens()){
                            msg = msg +" "+ st.nextToken();
                        }
                        Socket socket = GUI.getClientList(a);
                        try {
                            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                            String content = da +": "+ msg;
                            dos.writeUTF("CMD_MESSAGE "+ content);
                            GUI.appendMessage("[Message]: From "+ da +" To "+ a +" : "+ msg);
                        } catch (IOException e) {  GUI.appendMessage("[IOException]: Unable to send message to "+ a); }
                        break;
                    
                    case "CMD_CHATALL":
                        String chatcompletada = st.nextToken();
                        String chatcompletamsg = "";
                        while(st.hasMoreTokens()){
                            chatcompletamsg = chatcompletamsg +" "+st.nextToken();
                        }
                        String chatcompleta = chatcompletada +" "+ chatcompletamsg;
                        for(int i = 0; i < GUI.clientList.size(); i++){
                            if(!GUI.clientList.get(i).equals(chatcompletada)){
                                try {
                                    Socket socket2 = (Socket) GUI.socketList.get(i);
                                    DataOutputStream dos2 = new DataOutputStream(socket2.getOutputStream());
                                    dos2.writeUTF("CMD_MESSAGE "+ chatcompleta);
                                } catch (IOException e) {
                                    GUI.appendMessage("[CMD_CHATALL]: "+ e.getMessage());
                                }
                            }
                        }
                        GUI.appendMessage("[CMD_CHATALL]: "+ chatcompleta);
                        break;
                    default: 
                        GUI.appendMessage("[CMDException]: Unknown Command "+ CMD);
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println(client);
            GUI.appendMessage("[SocketThread]: Connessione client chiusa!");
        }
    }
    
}

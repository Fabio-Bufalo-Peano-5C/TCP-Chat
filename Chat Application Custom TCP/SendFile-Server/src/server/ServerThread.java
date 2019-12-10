
package servertcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author fabio
 */
public class ServerThread implements Runnable {
    
    ServerSocket server;
    GUIServer GUI;
    
    public ServerThread(int porta, GUIServer GUI){
        GUI.appendMessage("[Server]: Server in avvio sulla porta: "+ porta);
        try {
            this.GUI = GUI;
            server = new ServerSocket(porta);
            GUI.appendMessage("[Server]: Server avviato!");
        } 
        catch (IOException ex ) { GUI.appendMessage("[IOException]: "+ ex.getMessage()); } 
        catch (Exception ex ){ GUI.appendMessage("[Exception]: "+ ex.getMessage()); }
    }

    @Override
    public void run() {
        try {
            while(true){
                Socket socket = server.accept();
                new Thread(new SocketThread(socket, GUI)).start();
            }
        } catch (IOException e) {
            GUI.appendMessage("[ServerThreadIOException]: "+ e.getMessage());
        }
    }
    
    
    public void stop(){
        try {
            server.close();
            System.out.println("Il server è stato arrestato!");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
}

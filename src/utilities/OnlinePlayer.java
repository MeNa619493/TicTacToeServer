/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

/**
 *
 * @author Mina
 */
public class OnlinePlayer extends Thread{
   private Boolean loggedin;
   private Server server;
   private DataInputStream dis;
   private PrintStream ps;
   private Socket currentSocket;
    
    public OnlinePlayer(Socket socket){
       loggedin = false;
       System.out.println("start OnlinePlayer");
       server = Server.getServer();
       try {
            dis = new DataInputStream(socket.getInputStream());
            ps = new PrintStream(socket.getOutputStream());
            currentSocket = socket;
            this.start();
       }catch (IOException ex) {
           System.out.println("problem in streams OnlinePlayer");
            ex.printStackTrace();
            // alert 
           try {
               socket.close();
           } catch (IOException e) {
               System.out.println("problem in close socket in case IOException OnlinePlayer");
               e.printStackTrace();
           }
       }
   }
}

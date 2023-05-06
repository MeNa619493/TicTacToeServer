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
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

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
  private StringTokenizer token;
   DataAccessLayer database;


    
    public OnlinePlayer(Socket socket){
       loggedin = false;
       database = DataAccessLayer.getInstance();
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
    
    
    private void SignUp(){
        String username = token.nextToken();
                            String email = token.nextToken();
                            String password = token.nextToken();
                            System.out.println(username+" "+email+" "+password);
                             String check;
       try {
           check = database.validateRegister(email);
           if(check.equals("Registered Successfully")){
               // ps.println("Registered Successfully");

               database.signUp(email,username,password);
               
               System.out.println("User is registered now , check database");   

           }
           else if (check.equals("already signed-up")){
                //ps.println("already signed-up");
            }
       } catch (Exception ex) {
           Logger.getLogger(OnlinePlayer.class.getName()).log(Level.SEVERE, null, ex);
       }
                            
    
    }
}

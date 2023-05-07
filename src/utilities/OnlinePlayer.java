/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import Assets.ServerUiClass;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

import java.util.List;

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

   private String clientData,query;
   private StringTokenizer token;
   Thread thread;
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
     public void run(){
        if (server!=null){
            while(currentSocket.isConnected()){
                try {
                    clientData = dis.readLine();
                    if(clientData != null){
                        System.out.println(clientData);
                        token = new StringTokenizer(clientData,"####");
                        query = token.nextToken();
                        switch(query){

                            case "playerlist":
                                pushAvliableFriend();
                                break;
                            case "SignUp" :
                                SignUp();
                                    

                            default :
                                break;
                        }
                   }
                } catch (IOException ex) {
                    System.out.println("Problem in reading from stream in OnlinePlayer");
                    ex.printStackTrace();
                    try {
                        currentSocket.close();
                        System.out.println("socket closed");
                    } catch (IOException e) {
                         System.out.println("Problem in socket closed in OnlinePlayer");
                         e.printStackTrace();
                    }
                    this.stop();
                }
            }
        }
    }
     
     public void pushAvliableFriend() {
        thread =  new Thread(new Runnable() {
        @Override
            public void run() {
                while(true){
                    if(ServerUiClass.serverState){
                        List<String> availableFriends = server.database.showAvailableFriend();

                            for(String name: availableFriends){
                                ps.println(name+"###");
                            }
                            ps.println("null");

                       try {
                        Thread.sleep(5000);
                        } catch (InterruptedException ex) {
                            System.out.println("Error while thread sleep pushAvliableFriend");
                        }
                    }else{
                        ps.println("close");
                        thread.stop();
                    }
                }
            }
        });
        thread.start();
    }
     
     
     
}

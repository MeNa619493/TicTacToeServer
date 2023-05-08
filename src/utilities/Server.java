/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import Assets.ServerUiClass;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author moham
 */
public class Server {

    private static Server server;
    public DataAccessLayer database ;
    private ServerSocket serverSocket ;
    private Socket clientSocket ;
    private Thread listenerThread;
    private ArrayList<Socket> listOfClientSockets = new ArrayList();
    
    private Server(){
        try {
            startConnection();
        } catch (SQLException ex) {
            Logger.getLogger(ServerUiClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Server getServer(){
        if(server == null){
            server = new Server();
        }
        return server;
    }
    
    public void startConnection() throws SQLException{
        database = DataAccessLayer.getInstance();
        database.defaultStatus();
        startServer();
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(5005);
            listenerThread = new Thread(() -> {
                while (true) {
                    try {
                        clientSocket = serverSocket.accept();
                        new OnlinePlayer(clientSocket);
                        listOfClientSockets.add(clientSocket);
                        System.out.println("new player is created");   

                    } catch (IOException ex) {

                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        try {
                            serverSocket.close();
                            listenerThread.stop();
                        } catch (IOException ex1) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex1);
                        }
                    }
                }
            });
            listenerThread.start();
        } catch (IOException ex) {
            System.out.println("server Error");
            ex.printStackTrace();
        }
    }
    
    public int getOnline(){
        return database.getOnlinePlayers();
    }
    
    public int getOnlineCount(){
        return database.getOnlinePlayers();
    }
    
    public int getOfflineCount(){
        return database.getOfflinePlayers();
    }
    
    public String getUsername(String email){
        return database.getUsername(email);
    }
    
    public void closeClientsSockets(){
        try{
            for(Socket clientSocket: listOfClientSockets){
                clientSocket.close();
            }
        }catch(IOException ex) {
            System.out.print("problem in closing clients sockets in closeClientsSockets");
        }  
    }

    public void stopServer() {
        try {
            database.close();
            listenerThread.stop();
            serverSocket.close();
        } catch (SQLException ex) {
            System.out.print("problem in closing database connection in stopServer");
        } catch (IOException ex) {
            System.out.print("problem in closing server sockets in stopServer");
        }
    }

    
}

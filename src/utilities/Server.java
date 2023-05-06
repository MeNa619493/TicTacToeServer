/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
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
        
    }
    
    public static Server getServer(){
        if(server == null){
            server = new Server();
        }
        return server;
    }
    
    public void startConnection() throws SQLException{
        database = DataAccessLayer.getInstance();
        //database.resetStatus();
        //databaseInstance.disableConnection();
        //database.selectResultSet();
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

    public void stopServer() {
        try {
            database.close();
            listenerThread.stop();
            serverSocket.close();
        } catch (SQLException ex) {
            System.out.print("Error while closing database connection in stopServer");
        } catch (IOException ex) {
            System.out.print("Error while closing server cocket in stopServer");
        }
    }

    public void pushAvliableFriend() {
        ObjectOutputStream out = null;
        try {
            DataAccessLayer da = DataAccessLayer.getInstance();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            out = new ObjectOutputStream(bos);
            out.writeObject(da.showAvailableFriend());
            byte[] friendBytes = bos.toByteArray();
            OutputStream clientOutput = clientSocket.getOutputStream();
            clientOutput.write(friendBytes);
            clientOutput.flush();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

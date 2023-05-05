/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author moham
 */
public class Server {
    Thread listenerThread;
    private Socket clientSocket;
    private ServerSocket serverSocket;

    public void startServer() {
        if (listenerThread != null && listenerThread.isAlive()) {
            return;
        }

        try {
            serverSocket = new ServerSocket(5005);
            listenerThread = new Thread(() -> {
                while (true) {
                    try {
                        clientSocket = serverSocket.accept();
                    } catch (IOException ex) {
                        
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
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
        if (listenerThread == null || !listenerThread.isAlive()) {
            return;
        }

        try {
            listenerThread.stop();
            serverSocket.close();
            //if (clientSocket != null) {
            //    clientSocket.close();
            //}
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

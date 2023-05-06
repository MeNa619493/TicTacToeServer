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
        try {
            serverSocket = new ServerSocket(5005);
            listenerThread = new Thread(() -> {
                while (true) {
                    try {
                        clientSocket = serverSocket.accept();

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
        if (listenerThread == null || !listenerThread.isAlive()) {
            return;
        }

        try {
            listenerThread.stop();
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
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

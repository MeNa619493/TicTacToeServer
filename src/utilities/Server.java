/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author moham
 */
public class Server {
      private Socket clientSocket ;
      private ServerSocket serverSocket ;
      public DataAccessLayer database;
      
      public void makeconnectionDataBase(){
          database = DataAccessLayer.getInstance();
      
      }
      


    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.derby.jdbc.ClientDriver;

/**
 *
 * @author Mina
 */
public class DataAccessLayer {
    private static DataAccessLayer instance = null;
    private Connection con;
    private ResultSet rs;
    PreparedStatement prst ;
    int isDone;
    String dataBaseUrl = "jdbc:derby://localhost:1527/tictactoe2";
    private static final String TABLE_NAME = "PLAYER";
    private DataAccessLayer(){}
    
    public static synchronized DataAccessLayer getInstance(){
        if (instance == null){
            instance = new DataAccessLayer();
        }
        
        return instance;
    }
    
    public void connect() throws SQLException{
        DriverManager.registerDriver(new ClientDriver());
        con = DriverManager.getConnection(dataBaseUrl,
        "root","root");
        System.out.println("connection is Done");
    }
    
    public synchronized void  signUp (String email, String username , String password) throws SQLException{
    String Stmt = "insert into "+ TABLE_NAME +" (USERNAME,EMAIL,PASSWORD) values(?,?,?)";        prst= con.prepareStatement(Stmt) ;
        prst.setString(1, username);
        prst.setString(2, email);
        prst.setString(3, password);
        isDone= prst.executeUpdate();
            if(isDone>0) 
            {System.out.println("Insert Done");
            }
            else
            {System.out.println("Cann't insert");
            }
    
    
            
    }
     public synchronized String validateRegister(String email)throws Exception{
        String stmt="SELECT EMAIL FROM "+TABLE_NAME+ " WHERE EMAIL = ?";
        ResultSet rs;
        
            prst = con.prepareStatement(stmt, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            prst.setString(1, email);
            rs = prst.executeQuery();
            if(rs.next()){
                return "already signed-up";
            }
            else{
               return "Registered Successfully";
            }
       
    }
    
    public void close() throws SQLException{
       con.close();
    }
    
    
}

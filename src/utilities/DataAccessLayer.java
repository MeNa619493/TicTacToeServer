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
    String dataBaseUrl = "jdbc:derby://localhost:1527/TicTacToe";
    private static final String TABLE_NAME = "PLAYER";
    public DataAccessLayer(){}
    
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
    
    public synchronized void  signUp (Player p) throws SQLException{
        String Stmt = "insert into"+ TABLE_NAME+"(USERNAME,EMAIL,PASSWORD) values(?,?,?)";
        prst= con.prepareStatement(Stmt) ;
        prst.setString(1, p.getUsername());
        prst.setString(2, p.getEmail());
        prst.setString(3, p.getPassword());
        isDone= prst.executeUpdate();
            if(isDone>0){System.out.println("Insert Done");}
            else{System.out.println("Cann't insert");}
    
    
    }
     public synchronized String validateRegister(String userName){
        String stmt="select USERNAME from "+TABLE_NAME+ "where USERNAME=?";
        PreparedStatement pStmt;
        ResultSet rs;
        try {
            pStmt = con.prepareStatement(stmt, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pStmt.setString(1, userName);
            rs = pStmt.executeQuery();
            if(rs.next()){
                return "already signed-up";
            }
            else{
               return "Registered Successfully";
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return "Connection Issues";
        }
    }
    
    public void close() throws SQLException{
       con.close();
    }
    
    
}

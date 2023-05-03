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
    
    private DataAccessLayer(){}
    
    public static synchronized DataAccessLayer getInstance(){
        if (instance == null){
            instance = new DataAccessLayer();
        }
        
        return instance;
    }
    
    public void connect() throws SQLException{
        DriverManager.registerDriver(new ClientDriver());
        con = DriverManager.getConnection("",
        "root","root");
    }
    
    
    
    public void close() throws SQLException{
       con.close();
    }
    
    
}

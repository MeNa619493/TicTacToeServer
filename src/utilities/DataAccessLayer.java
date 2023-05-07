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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.derby.jdbc.ClientDriver;

/**
 *
 * @author Mina
 */
public class DataAccessLayer {

    private static DataAccessLayer instance = null;
    private Connection con;
    private ResultSet rs;
    PreparedStatement prst;
    int isDone;
    String dataBaseUrl = "jdbc:derby://localhost:1527/tictactoe";
    private static final String TABLE_NAME = "PLAYER";


    private DataAccessLayer() {
        try {
            this.connect();
        } catch (SQLException ex) {
            System.out.println("Database connection problem");
            ex.printStackTrace();
        }
    }

    public static synchronized DataAccessLayer getInstance() {
        if (instance == null) {

            instance = new DataAccessLayer();
        }

        return instance;
    }

    private void connect() throws SQLException {
        DriverManager.registerDriver(new ClientDriver());
        con = DriverManager.getConnection(dataBaseUrl,
                "root", "root");
        System.out.println("connection is Done");
    }
    
    public synchronized void  signUp (String email, String username , String password) throws SQLException{
    String Stmt = "insert into "+ TABLE_NAME +" (USERNAME,EMAIL,PASSWORD) values(?,?,?)";    
    prst= con.prepareStatement(Stmt) ;
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
            } else {
                return "Registered Successfully";
            }
       
    }
     public synchronized String validateLogin(String email, String password) throws SQLException {
    String stmt = "SELECT * FROM " + TABLE_NAME + " WHERE EMAIL = ? AND PASSWORD = ?";
    ResultSet rs;

    prst = con.prepareStatement(stmt, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
    prst.setString(1, email);
    prst.setString(2, password);
    rs = prst.executeQuery();

    if (rs.next()) {
        // Check if the user is already signed in
        boolean isActive = rs.getBoolean("ISACTIVE");
        if (isActive) {
            return "This Email is already signed in";
        } else {
            // Set user status to active
            String updateStmt = "UPDATE " + TABLE_NAME + " SET ISACTIVE = ? WHERE EMAIL = ?";
            prst = con.prepareStatement(updateStmt);
            prst.setBoolean(1, true);
            prst.setString(2, email);
            prst.executeUpdate();
            return "Login Successful";
        }
    } else {
        return "Invalid Email or Password";
    }
}

    public synchronized int getOnlinePlayers() {
        int rows = 0;
        try {
            prst = con.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE ISACTIVE = TRUE",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            rs = prst.executeQuery();
            if (rs.last()) {
                rows = rs.getRow();
            }
            return rows;
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("catch isactive=true");
            return -1;
        }
    }

    public synchronized int getOfflinePlayers() {
        int rows = 0;
        try {
            prst = con.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE ISACTIVE = FALSE",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            rs = prst.executeQuery();
            if (rs.last()) {
                rows = rs.getRow();
            }
            return rows;
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("catch isactive=false");
            return -1;
        }
    }
    
    public synchronized void defaultStatus() throws SQLException{
        makePlayersNotPlaying();
        makeAllPlayersOffline();
    }
    
    public synchronized void makePlayersNotPlaying(){
         try {
            prst = con.prepareStatement("update " + TABLE_NAME + " set isPlay = ? ",
                    ResultSet.TYPE_SCROLL_SENSITIVE ,ResultSet.CONCUR_UPDATABLE);
            prst.setString(1, "false");
            prst.executeUpdate();
            
        } catch (SQLException ex) {
            System.out.println("problem in makePlayersNotPlaying");
            ex.printStackTrace();
        }
    }

    public synchronized void makeAllPlayersOffline(){
        try {
            prst = con.prepareStatement("update " + TABLE_NAME + " set isActive = ? ",
                    ResultSet.TYPE_SCROLL_SENSITIVE ,ResultSet.CONCUR_UPDATABLE);
            prst.setString(1, "false");
            prst.executeUpdate();
            
        } catch (SQLException ex) {
            System.out.println("problem in makeAllPlayersOffline");
            ex.printStackTrace();
        }
        
    }
    
    public void close() throws SQLException{
       System.out.println("connection closed");
       defaultStatus();
       rs.close();
       prst.close();
       con.close();
       instance = null;
    }

    public synchronized List<String> showAvailableFriend() {
        List<String> availableFriends = new ArrayList<>();
        try {
            prst = con.prepareStatement("SELECT USERNAME FROM " + TABLE_NAME + " WHERE ISACTIVE = TRUE AND ISPLAY = FALSE",
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            rs = prst.executeQuery();
            while (rs.next()) {
                String friendName = rs.getString("USERNAME");
                availableFriends.add(friendName);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error retrieving available friends");
        }
        return availableFriends;
    }


}

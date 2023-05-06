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
    String dataBaseUrl = "jdbc:derby://localhost:1527/TicTacToe";
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

    public synchronized void signUp(Player p) throws SQLException {
        String Stmt = "insert into" + TABLE_NAME + "(USERNAME,EMAIL,PASSWORD) values(?,?,?)";
        prst = con.prepareStatement(Stmt);
        prst.setString(1, p.getUsername());
        prst.setString(2, p.getEmail());
        prst.setString(3, p.getPassword());

        isDone = prst.executeUpdate();
        if (isDone > 0) {
            System.out.println("Insert Done");
        } else {
            System.out.println("Cann't insert");
        }

    }

    public synchronized String validateRegister(String userName) {
        String stmt = "select USERNAME from " + TABLE_NAME + "where USERNAME=?";
        PreparedStatement pStmt;
        ResultSet rs;
        try {
            pStmt = con.prepareStatement(stmt, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pStmt.setString(1, userName);
            rs = pStmt.executeQuery();
            if (rs.next()) {
                return "already signed-up";
            } else {
                return "Registered Successfully";
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return "Connection Issues";
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

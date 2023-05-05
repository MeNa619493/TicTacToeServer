/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;
import org.apache.derby.jdbc.ClientDriver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utilities.Player;
/**
 *
 * @author USER
 */
//public class DataBase {
//    public Connection con;
//    private PreparedStatement preStmt;
//    private ResultSet result;
//    
//    private DataBase() throws SQLException{
//        DriverManager.registerDriver(new ClientDriver());
//        //con = DriverManager.getConnection("jdbc:derby://localhost:1527/SignDataBase","root","root");
//        
//    }
// public synchronized ResultSet executeQuery(){
//    try {
//        this.preStmt = con.prepareStatement("Select * from SIGN", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
//        return preStmt.executeQuery();
//    } catch (SQLException ex) {
//        Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
//    }
//    return null;
//}
//
//    public synchronized Player getPlayer(String email){
//        String stmt = "select * from Player where email=?";
//        PreparedStatement pStmt;
//        ResultSet rs;
//        Player p;
//        try {
//            pStmt = con.prepareStatement(stmt, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            pStmt.setString(1,email);
//            rs = pStmt.executeQuery();
//            if(rs.next()){
//                p=new Player();
//                p.setUserName(rs.getString(1));
//                p.setEmail(rs.getString(2));
//                p.setPassword(rs.getString(3));
//                p.setIsActive(rs.getBoolean(4));
//                p.setIsPlaying(rs.getBoolean(5));
//                p.setScore(rs.getInt(6));
//                return p;
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//    public synchronized boolean checkIsActive(String email){
//        String stmt = "select isActive from Player where email=?";
//        PreparedStatement pStmt;
//        ResultSet rs;
//        try {
//            pStmt = con.prepareStatement(stmt, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            pStmt.setString(1,email);
//            rs = pStmt.executeQuery();
//            if(rs.next()){
//                if(rs.getBoolean(1)){
//                    return true;
//                }
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return false;
//    }
//       public synchronized String validationLogin(String email,String password){
//        Player  ptemp = this.getPlayer(email);
//        if(ptemp!=null){
//            String pass=ptemp.getPassword();
//            if(pass.equals(password)){
//                if(checkIsActive(email)){
//                    return "This Email is alreay sign-in";
//                }
//                else{
//                    return "Logged in successfully";
//                }
//            }
//            else{
//                return "Password is incorrect";
//            }
//        }
//        else{
//            return "Email is incorrect";
//        }
//    }
//    
//    
//    public synchronized void login(String email,String password){
//      checkActivation(true,email);
//    }
//        //update player data
//    public synchronized void checkActivation(boolean state,String email){
//       
//        try {
//            preStmt = con.prepareStatement("update player set isActive = ? where email = ?");
//            preStmt.setBoolean(1, state);
//            preStmt.setString(2, email);
//            preStmt.executeUpdate();
//        } catch (SQLException ex) {
//            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        executeQuery();
//    }
//    
//}

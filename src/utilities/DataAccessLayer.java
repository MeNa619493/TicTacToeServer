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
    public synchronized void executeQuery(){
    try {
        this.preStmt = con.prepareStatement("Select * from"+TABLE_NAME, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
      // return preStmt.executeQuery();
    } catch (SQLException ex) {
        Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
    }
    //return null;
}

    public synchronized Player getPlayer(String email){
        String stmt = "select * from Player where email=?";
        PreparedStatement pStmt;
        ResultSet rs;
        Player p;
        try {
            pStmt = con.prepareStatement(stmt, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pStmt.setString(1,email);
            rs = pStmt.executeQuery();
            if(rs.next()){
                p=new Player();
                p.setUserName(rs.getString(1));
                p.setEmail(rs.getString(2));
                p.setPassword(rs.getString(3));
                p.setIsActive(rs.getBoolean(4));
                p.setIsPlaying(rs.getBoolean(5));
                p.setScore(rs.getInt(6));
                return p;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public synchronized boolean checkIsActive(String email){
        String stmt = "select isActive from Player where email=?";
        PreparedStatement pStmt;
        ResultSet rs;
        try {
            pStmt = con.prepareStatement(stmt, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pStmt.setString(1,email);
            rs = pStmt.executeQuery();
            if(rs.next()){
                if(rs.getBoolean(1)){
                    return true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
       public synchronized String validationLogin(String email,String password){
        Player  ptemp = this.getPlayer(email);
        if(ptemp!=null){
            String pass=ptemp.getPassword();
            if(pass.equals(password)){
                if(checkIsActive(email)){
                    return "This Email is alreay sign-in";
                }
                else{
                    return "Logged in successfully";
                }
            }
            else{
                return "Password is incorrect";
            }
        }
        else{
            return "Email is incorrect";
        }
    }
      
    public synchronized void login(String email,String password){
      checkActivation(true,email);
    }
        //update player data
    public synchronized void checkActivation(boolean state,String email){
       
        try {
            preStmt = con.prepareStatement("update player set isActive = ? where email = ?");
            preStmt.setBoolean(1, state);
            preStmt.setString(2, email);
            preStmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        executeQuery();
    }
    public void close() throws SQLException{
       con.close();
    }
    
    
}

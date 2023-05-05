package Assets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Arc;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONException;
import org.json.JSONObject;
import tictactoeserver.TicTacToeServer;
import utilities.DataAccessLayer;

public  class ServerUiClass extends AnchorPane {

    protected final Text text;
    protected final Text text0;
    protected final Arc onlineArc;
    protected final Arc OfflineArc;
    protected final ToggleButton btnServerState;
    protected final Text text1;
    protected final Text text2;
    protected final Text NumberOfOnline;
    protected final Text NumberOfOffline;
    private StringTokenizer token;

    ServerSocket serverSocket;
    DataInputStream dis;
    PrintStream ps;
    Thread thread ;
    Socket client ;
    DataAccessLayer database;
    int type ;
    String username;
    String email;
    String password;

    public ServerUiClass() {
        database = DataAccessLayer.getInstance();

        text = new Text();
        text0 = new Text();
        onlineArc = new Arc();
        OfflineArc = new Arc();
        btnServerState = new ToggleButton();
        text1 = new Text();
        text2 = new Text();
        NumberOfOnline = new Text();
        NumberOfOffline = new Text();

        setId("AnchorPane");
        setPrefHeight(600.0);
        setPrefWidth(600.0);

        text.setFill(javafx.scene.paint.Color.valueOf("#0070fc"));
        text.setLayoutX(14.0);
        text.setLayoutY(53.0);
        text.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        text.setStrokeWidth(0.0);
        text.setText("Tic Tac Toe Server");
        text.setFont(new Font("System Bold", 36.0));

        text0.setFill(javafx.scene.paint.Color.RED);
        text0.setLayoutX(14.0);
        text0.setLayoutY(107.0);
        text0.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        text0.setStrokeWidth(0.0);
        text0.setText("Player State");
        text0.setFont(new Font("System Bold", 24.0));

        onlineArc.setFill(javafx.scene.paint.Color.valueOf("#ff1f1f"));
        onlineArc.setLayoutX(302.0);
        onlineArc.setLayoutY(250.0);
        onlineArc.setLength(270.0);
        onlineArc.setRadiusX(150.0);
        onlineArc.setRadiusY(150.0);
        onlineArc.setStartAngle(45.0);
        onlineArc.setStroke(javafx.scene.paint.Color.BLACK);
        onlineArc.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        onlineArc.setType(javafx.scene.shape.ArcType.ROUND);

        OfflineArc.setFill(javafx.scene.paint.Color.LIME);
        OfflineArc.setLayoutX(301.0);
        OfflineArc.setLayoutY(250.0);
        OfflineArc.setLength(90.0);
        OfflineArc.setRadiusX(150.0);
        OfflineArc.setRadiusY(150.0);
        OfflineArc.setStartAngle(-45.0);
        OfflineArc.setStroke(javafx.scene.paint.Color.BLACK);
        OfflineArc.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        OfflineArc.setType(javafx.scene.shape.ArcType.ROUND);

        btnServerState.setGraphicTextGap(1.0);
        btnServerState.setLayoutX(498.0);
        btnServerState.setLayoutY(554.0);
        btnServerState.setMnemonicParsing(false);
        btnServerState.setText("Server State");
        btnServerState.setTextFill(javafx.scene.paint.Color.WHITE);

        text1.setFill(javafx.scene.paint.Color.RED);
        text1.setLayoutX(154.0);
        text1.setLayoutY(473.0);
        text1.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        text1.setStrokeWidth(0.0);
        text1.setText("Online");
        text1.setFont(new Font("System Bold", 24.0));

        text2.setFill(javafx.scene.paint.Color.LIME);
        text2.setLayoutX(392.0);
        text2.setLayoutY(472.0);
        text2.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        text2.setStrokeWidth(0.0);
        text2.setText("Offline");
        text2.setFont(new Font("System Bold", 24.0));

        NumberOfOnline.setLayoutX(172.0);
        NumberOfOnline.setLayoutY(509.0);
        NumberOfOnline.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        NumberOfOnline.setStrokeWidth(0.0);
        NumberOfOnline.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        NumberOfOnline.setFont(new Font("System Bold", 18.0));

        NumberOfOffline.setLayoutX(412.0);
        NumberOfOffline.setLayoutY(508.0);
        NumberOfOffline.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        NumberOfOffline.setStrokeWidth(0.0);
        NumberOfOffline.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        NumberOfOffline.setFont(new Font("System Bold", 18.0));

        getChildren().add(text);
        getChildren().add(text0);
        getChildren().add(onlineArc);
        getChildren().add(OfflineArc);
        getChildren().add(btnServerState);
        getChildren().add(text1);
        getChildren().add(text2);
        getChildren().add(NumberOfOnline);
        getChildren().add(NumberOfOffline);
        setStyle("-fx-background-image: url('file:./src/Assets/bgGp.jpg');"
                + "-fx-background-size: cover;"
                + "-fx-background-position: center center;");
        btnServerState.setId("myButton");
    
    
        
        
       
       
        thread= new Thread(() -> {
            try {
                
                serverSocket = new ServerSocket(5006);
                while (true){
                   client= serverSocket.accept();
                     dis = new DataInputStream(client.getInputStream());
                      String data =dis.readLine();
                      System.out.println(dis.readLine());
                      token = new StringTokenizer(data,"####");
                            String type= token.nextToken();

                            String username = token.nextToken();
                            email = token.nextToken();
                            String password = token.nextToken();
                            System.out.println(username+" "+email+" "+password);
                             String check;
                            check = database.validateRegister(email);
                            System.out.println(check);
                            ps.println(check);
                             if(check.equals("Registered Successfully")){
                
                                    database.signUp(username,email,password);
                                    System.out.println("User is registered now , check database");   
                                    }
                             else if (check.equals("already signed-up")){
                                     ps.println("already signed-up");
            }
                            
                            
                
                
                }}
                     
                
            catch(Exception e){ 
            System.out.println("Connection Issues");}
                    
                       
            
            
              
            
        });
        thread.start();
  
        
        
        
        
        
        
        
        
        
   
}



   }


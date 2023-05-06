/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeserver;

import Assets.ServerUiClass;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilities.DataAccessLayer;

/**
 *
 * @author Mina
 */
public class TicTacToeServer extends Application {
    
    @Override
    public void start(Stage primaryStage) {

        ServerUiClass root = new ServerUiClass();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("file:./src/Assets/buttonStyle.css");
        primaryStage.setTitle("Server");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show(); 
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        
    }
    
}

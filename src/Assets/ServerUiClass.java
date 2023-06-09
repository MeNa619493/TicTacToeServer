package Assets;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.time.Duration;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import utilities.Server;

public class ServerUiClass extends AnchorPane {

    protected final PieChart pcPlayerStates;
    protected final Text text;
    protected final Text text0;
    protected final ToggleButton btnServerState;
    protected final Text text1;
    protected final Text text2;
    protected final Text NumberOfOnline;
    protected final Text NumberOfOffline;
    private StringTokenizer token;

    int type;
    String username;
    String email;
    String password;
    ServerSocket serverSocket;
    DataInputStream dis;
    PrintStream ps;
    Thread thread;
    Socket client;
    Server server;
    private Thread chartThread;
    int onlinePlayersNo = 0;
    int offlinePlayersNo = 0;
    int oldOnlinePlayersNo = -1;
    int oldOfflinePlayersNo = -1;
    public static boolean serverState = true;

    public ServerUiClass() {
        server = Server.getServer();
        pcPlayerStates = new PieChart();
        text = new Text();
        text0 = new Text();
        btnServerState = new ToggleButton();
        text1 = new Text();
        text2 = new Text();
        NumberOfOnline = new Text();
        NumberOfOffline = new Text();
        setId("AnchorPane");
        setPrefHeight(600.0);
        setPrefWidth(600.0);

        pcPlayerStates.setLayoutX(146.0);
        pcPlayerStates.setLayoutY(149.0);
        pcPlayerStates.setPrefHeight(278.0);
        pcPlayerStates.setPrefWidth(348.0);

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

        getChildren().add(pcPlayerStates);
        getChildren().add(text);
        getChildren().add(text0);
        getChildren().add(btnServerState);
        getChildren().add(text1);
        getChildren().add(text2);
        getChildren().add(NumberOfOnline);
        getChildren().add(NumberOfOffline);

        setStyle("-fx-background-image: url('file:./src/Assets/bgGp.jpg');"
                + "-fx-background-size: cover;"
                + "-fx-background-position: center center;");
        btnServerState.setId("myButton");

        btnServerState.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                serverState = !serverState;
                pcPlayerStates.setVisible(serverState);
                if (serverState) {
                    try {
                        server.startConnection();
                    } catch (SQLException ex) {
                        Logger.getLogger(ServerUiClass.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    chartThread.resume();
                    btnServerState.setId("myButton");
                    System.out.println("server Start");
                } else {
                    server.stopServer();
                    btnServerState.setId("myButtonOff");
                    System.out.println("server Off");
                }

                btnServerState.setDisable(true);
                Timeline timeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(2), e -> {
                    btnServerState.setDisable(false);
                }));
                timeline.play();
            }
        });

        observeChart();
    }

    private void observeChart() {

        chartThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (serverState) {
                        ObservableList<PieChart.Data> pieChartData;
                        offlinePlayersNo = server.getOfflineCount();
                        onlinePlayersNo = server.getOnlineCount();
                        //System.out.println("offlinePlayersNo = " + offlinePlayersNo);
                        //System.out.println("onlinePlayersNo = " + onlinePlayersNo);
                        pieChartData
                                = FXCollections.observableArrayList(
                                        new PieChart.Data("Online", onlinePlayersNo),
                                        new PieChart.Data("Offline", offlinePlayersNo));

                        Platform.runLater(() -> {
                            try {
                                if (oldOfflinePlayersNo != offlinePlayersNo || oldOnlinePlayersNo != onlinePlayersNo) {
                                    oldOfflinePlayersNo = offlinePlayersNo;
                                    oldOnlinePlayersNo = onlinePlayersNo;
                                    pcPlayerStates.setData(pieChartData);
                                }
                            } catch (Exception ex) {
                                System.out.println("Problem in chart thread");
                                ex.printStackTrace();
                            }
                        });
                        try {
                            chartThread.sleep(1000);
                        } catch (InterruptedException ex) {
                            System.out.println("observeChart");
                            ex.printStackTrace();
                        }
                    } else {
                        onlinePlayersNo = 0;
                        chartThread.suspend();
                    }
                    handleTextFields();
                }
            }
        });
        chartThread.start();
    }

    private void handleTextFields() {
        Platform.runLater(() -> {
            try {
                NumberOfOnline.setText("" + onlinePlayersNo);
                NumberOfOffline.setText("" + offlinePlayersNo);
            } catch (Exception ex) {
                System.out.println("Problem in chart thread");
                ex.printStackTrace();
            }
        });
    }

}

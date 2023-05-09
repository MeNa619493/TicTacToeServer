/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import Assets.ServerUiClass;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mina
 */
class OnlinePlayer extends Thread {

    private Server server;
    private DataInputStream dis;
    private PrintStream ps;
    private Socket currentSocket;
    private String clientData, query;
    private StringTokenizer token;
    Thread thread;
    DataAccessLayer database;
    static ArrayList<OnlinePlayer> OnlineUsers =  new ArrayList();
    private String username;
    private String password;
    private String email;

    public OnlinePlayer(Socket socket) {
        database = DataAccessLayer.getInstance();
        System.out.println("start OnlinePlayer");
        server = Server.getServer();
        try {
            dis = new DataInputStream(socket.getInputStream());
            ps = new PrintStream(socket.getOutputStream());
            currentSocket = socket;
            this.start();
        } catch (IOException ex) {
            System.out.println("problem in streams OnlinePlayer");
            ex.printStackTrace();
            // alert 
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("problem in close socket in case IOException OnlinePlayer");
                e.printStackTrace();

            }
        }
    }

    public void run() {
        if (server != null) {
            while (currentSocket.isConnected()) {
                try {
                    clientData = dis.readLine();
                    if (clientData != null) {
                        System.out.println(clientData);
                        token = new StringTokenizer(clientData, "####");
                        query = token.nextToken();
                        switch (query) {
                            case "SignIn":
                                SignIn();
                                break;
                            case "SignUp":
                                SignUp();
                                break;
                            case "playerlist":
                                pushAvliableFriend();
                                break;
                            case "request":
                                sendRequest();
                                for (OnlinePlayer user : OnlineUsers) {
                                    user.ps.println("requestPlaying");
                                }

                                break;

                            case "accept":
                                //acceptChallenge();
                                break;
                            case "decline":
                                //refusedChallenge();
                                break;

                            default:
                                break;
                        }
                    }
                } catch (IOException ex) {
                    System.out.println("Problem in reading from stream in OnlinePlayer");
                    ex.printStackTrace();
                    try {
                        currentSocket.close();
                        System.out.println("socket closed");
                    } catch (IOException e) {
                        System.out.println("Problem in socket closed in OnlinePlayer");
                        e.printStackTrace();
                    }
                    this.stop();
                }
            }
        }
    }

    private void SignIn() {
        email = token.nextToken();
        password = token.nextToken();
        System.out.println(email + " " + password);
        String check;
        try {
            check = database.validateLogin(email, password);
            if (check.equals("Login Successful")) {
                OnlineUsers.add(this);

                ps.println("Login Successful");
                System.out.println("User is Signed in ");
                username = server.getUsername(email);
                ps.println(username);

            } else if (check.equals("Invalid Email or Password")) {
                ps.println("Invalid Email or Password");
            }
        } catch (Exception ex) {
            Logger.getLogger(OnlinePlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void SignUp() {
        username = token.nextToken();
        email = token.nextToken();
        password = token.nextToken();
        System.out.println(username + " " + email + " " + password);
        String check;
        try {
            check = database.validateRegister(email);
            if (check.equals("Registered Successfully")) {
                ps.println("Registered Successfully");

                database.signUp(email, username, password);

                System.out.println("User is registered now , check database");

            } else if (check.equals("already signed-up")) {
                ps.println("already signed-up");
            }
        } catch (Exception ex) {
            Logger.getLogger(OnlinePlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void pushAvliableFriend() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (ServerUiClass.serverState) {
                        List<String> availableFriends = server.database.showAvailableFriend();

                        for (String name : availableFriends) {
                            ps.println(name + "###");
                        }
                        ps.println("null");

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException ex) {
                            System.out.println("Error while thread sleep pushAvliableFriend");
                        }
                    } else {
                        ps.println("close");
                        thread.stop();
                    }
                }
            }
        });
        thread.start();
    }

    private void sendRequest() {
        String secondPlayer = token.nextToken();
//        System.out.println(secondPlayer);
        String player1 = token.nextToken();
//        System.out.println(player1);

        for (OnlinePlayer user : OnlineUsers) {
            if (user.username.equals(secondPlayer)) {
                System.out.println(secondPlayer);
                user.ps.println("requestPlaying");
                user.ps.println(secondPlayer);
            }
        }
    }
    
    

}

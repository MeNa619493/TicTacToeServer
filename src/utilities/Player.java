/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

/**
 *
 * @author moham
 */
public class Player {
    private String username;
    private String email;
    private String password;
    private boolean isActive;
    private boolean isPlay;
    private int score;

    public Player(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.isActive = true;
        this.isPlay = false;
        this.score = 0;
    }

    // getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean isPlay) {
        this.isPlay = isPlay;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

package com.example.astero.myapplication01.websockets.action;

/**
 * Created by astero on 10/03/2017.
 */

public class ActionGuess extends Action {

    private String user;
    private String word;

    public ActionGuess(String user, String word) {
        super.action = "guess";
        this.user = user;
        this.word = word;
    }
    public ActionGuess() {

    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}

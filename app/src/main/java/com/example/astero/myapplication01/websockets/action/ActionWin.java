package com.example.astero.myapplication01.websockets.action;

/**
 * Created by astero on 10/03/2017.
 */

public class ActionWin extends Action {

    private String user;
    private String word;

    public ActionWin(String user, String word){
        super.action = "win";
        this.user = user;
        this.word = word;
    }
    public ActionWin(){}

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

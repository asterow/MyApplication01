package com.example.astero.myapplication01.websockets.action;

/**
 * Created by astero on 09/03/2017.
 */

public class ActionStartDraw extends Action {
    private String user;

    public ActionStartDraw(String user) {
        super.action = "startdraw";
        this.user = user;
    }

    public ActionStartDraw() {}

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}

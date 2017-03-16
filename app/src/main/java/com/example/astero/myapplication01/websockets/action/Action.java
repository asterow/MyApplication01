package com.example.astero.myapplication01.websockets.action;

/**
 * Created by astero on 09/03/2017.
 */

public abstract class Action {

    protected String action;

    public Action() {

    }



    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}

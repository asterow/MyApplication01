package com.example.astero.myapplication01.websockets.action;

/**
 * Created by astero on 09/03/2017.
 */

public class ActionMessage extends Action {
    private String message;

    public ActionMessage(String message) {
        super.action = "message";
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

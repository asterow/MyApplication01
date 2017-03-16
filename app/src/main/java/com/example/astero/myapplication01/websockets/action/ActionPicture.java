package com.example.astero.myapplication01.websockets.action;

/**
 * Created by astero on 09/03/2017.
 */

public class ActionPicture extends Action{
    private String b64;

    public ActionPicture(String b64) {
        super.action = "picture";
        this.b64 = b64;
    }

    public String getB64() {
        return b64;
    }

    public void setB64(String b64) {
        this.b64 = b64;
    }
}

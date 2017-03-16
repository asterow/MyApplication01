package com.example.astero.myapplication01.websockets.action;

/**
 * Created by astero on 09/03/2017.
 */

public class ActionDraw extends Action {
    private String type;
    private float x;
    private float y;


    public ActionDraw(String type, float x, float y) {
        super.action = "draw";
        this.type = type;
        this.x = x;
        this.y = y;

    }

    public ActionDraw(){}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }


}

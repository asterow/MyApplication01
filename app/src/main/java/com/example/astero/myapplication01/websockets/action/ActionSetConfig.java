package com.example.astero.myapplication01.websockets.action;

/**
 * Created by astero on 13/03/2017.
 */

public class ActionSetConfig extends Action {

    private float width;
    private float height;
    private int color;

    public ActionSetConfig(float width, float height, int color) {
        super.action = "setconfig";
        this.width = width;
        this.height = height;
        this.color = color;
    }
    public ActionSetConfig(){}

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}

package com.example.astero.myapplication01.websockets.action;

/**
 * Created by astero on 09/03/2017.
 */

public class ActionRegister extends Action{
    private String name;

    public ActionRegister(String name) {
        super.action = "register";
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

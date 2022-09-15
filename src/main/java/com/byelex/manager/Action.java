package com.byelex.manager;

import java.time.LocalTime;


public class Action {
    enum ActionType {
        turnOn,
        turnOff
    }

    LocalTime timeOn;
    LocalTime timeOff;

    public Action(LocalTime timeOn, LocalTime timeOff) {
        this.timeOn = timeOn;
        this.timeOff = timeOff;
    }

}
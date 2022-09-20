package com.byelex.entity;

import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Action {
    public enum ActionType {
        turnOn,
        turnOff
    }

    private LocalTime timeOn;
    private LocalTime timeOff;

    public Action(LocalTime timeOn, LocalTime timeOff) {
        this.timeOn = timeOn;
        this.timeOff = timeOff;
    }

}
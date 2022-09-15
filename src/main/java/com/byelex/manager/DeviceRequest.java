package com.byelex.manager;

import java.time.LocalDate;

public class DeviceRequest {
    String deviceID;
    LocalDate date;
    Action[] actions;

    public DeviceRequest(String deviceID, LocalDate date, Action[] actions) {
        this.deviceID = deviceID;
        this.date = date;
        this.actions = actions;
    }
}
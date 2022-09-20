package com.byelex.entity;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class DeviceRequest {
    private String deviceID;
    private LocalDate date;
    private Action[] actions;

    public DeviceRequest(String deviceID, LocalDate date, Action[] actions) {
        this.deviceID = deviceID;
        this.date = date;
        this.actions = actions;
    }
}
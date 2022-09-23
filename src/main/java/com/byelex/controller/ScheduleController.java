package com.byelex.controller;

import com.byelex.entity.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import com.byelex.service.SchedulerJobService;
import org.quartz.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ScheduleController {

    private final SchedulerJobService schedulerJobService;

    @PostMapping("/send_schedule")
    public void updateSchedule(@RequestBody DeviceRequest[] request) throws SchedulerException {

        for (DeviceRequest unit : request) {
            schedulerJobService.deleteJobsByDateAndDeviceId(unit.getDate(), unit.getDeviceID());
            for (Action action : unit.getActions()) {
                scheduleTumblerJob(unit.getDeviceID(), unit.getDate(), action.getTimeOff(), Action.ActionType.turnOff);
                scheduleTumblerJob(unit.getDeviceID(), unit.getDate(), action.getTimeOn(), Action.ActionType.turnOn);
            }
        }
    }

    @GetMapping("/get_schedule")
    public String getSchedule() {
        return Arrays.toString(schedulerJobService.getAllJobList().toArray());
    }

    public void scheduleTumblerJob(String deviceID, LocalDate date, LocalTime time, Action.ActionType action) throws SchedulerException {
        if (date.isAfter(LocalDate.now()) || (time.isAfter(LocalTime.now()) && date.isEqual(LocalDate.now()))) {
            System.out.println(LocalDate.now() + " " + LocalTime.now());
            SchedulerJobInfo info = new SchedulerJobInfo();
            info.setJobDeviceId(deviceID);
            info.setJobDate(date);
            info.setJobTime(time);
            info.setJobActionType(action);
            schedulerJobService.scheduleNewJob(info);

            System.out.println("scheduled to " + action.toString() + " " + deviceID + " device at " + date + " " + time);
        } else {
            System.out.println("not scheduled to " + action.toString() + " " + deviceID + " device at " + date + " " + time +
                    " because of wrong date and time value");
        }
    }

}
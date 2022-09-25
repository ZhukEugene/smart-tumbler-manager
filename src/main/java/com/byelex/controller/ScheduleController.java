package com.byelex.controller;

import com.byelex.entity.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import com.byelex.service.SchedulerJobService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ScheduleController {

    private final SchedulerJobService schedulerJobService;

    @PostMapping("/post_schedule")
    public void updateSchedule(@RequestBody DeviceRequest[] request) {

        for (DeviceRequest deviceRequest : request) {
            schedulerJobService.deleteJobsByDateAndDeviceId(deviceRequest.getDate(), deviceRequest.getDeviceID());

            for (Action action : deviceRequest.getActions()) {

                if (deviceRequest.getDate().isAfter(LocalDate.now()) || (action.getTimeOff().isAfter(LocalTime.now()) && deviceRequest.getDate().isEqual(LocalDate.now()))) {
                    schedulerJobService.scheduleNewJob(deviceRequest.getDeviceID(), deviceRequest.getDate(), action.getTimeOff(), Action.ActionType.turn_off);

                    System.out.println("scheduled to " + Action.ActionType.turn_off + " " + deviceRequest.getDeviceID() + " device at " + deviceRequest.getDate() + " " + action.getTimeOff());
                } else {
                    System.out.println("not scheduled to " + Action.ActionType.turn_off + " " + deviceRequest.getDeviceID() + " device at " + deviceRequest.getDate() + " " + action.getTimeOff() +
                            " because of wrong date and time value");
                }

                if (deviceRequest.getDate().isAfter(LocalDate.now()) || (action.getTimeOn().isAfter(LocalTime.now()) && deviceRequest.getDate().isEqual(LocalDate.now()))) {
                    schedulerJobService.scheduleNewJob(deviceRequest.getDeviceID(), deviceRequest.getDate(), action.getTimeOn(), Action.ActionType.turn_on);

                    System.out.println("scheduled to " + Action.ActionType.turn_on + " " + deviceRequest.getDeviceID() + " device at " + deviceRequest.getDate() + " " + action.getTimeOn());
                } else {
                    System.out.println("not scheduled to " + Action.ActionType.turn_on + " " + deviceRequest.getDeviceID() + " device at " + deviceRequest.getDate() + " " + action.getTimeOn() +
                            " because of wrong date and time value");
                }
            }
        }
    }

    @GetMapping("/get_schedule")
    public String getSchedule() {
        return Arrays.toString(schedulerJobService.getAllJobList().toArray());
    }
    @GetMapping("/clear_schedule")
    public void clearSchedule() {
        schedulerJobService.deleteAllJobs();
    }

}
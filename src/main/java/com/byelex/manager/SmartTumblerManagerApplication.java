package com.byelex.manager;

import java.time.LocalDateTime;
import java.util.*;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@SpringBootApplication
@RestController
public class SmartTumblerManagerApplication {

    Integer jobNum = 0;
    Scheduler scheduler;
    ArrayList<JobKey> jobs = new ArrayList<>();

    public static void main(String[] args) {
        SpringApplication.run(SmartTumblerManagerApplication.class, args);
    }


    @PostMapping("/send_schedule")
    public void createSchedule(@RequestBody DeviceRequest[] request) throws SchedulerException {

        if (scheduler == null) {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
        } else {
            scheduler.deleteJobs(jobs);
            jobs.clear();
        }

        if (!scheduler.isStarted()) {
            scheduler.start();
        }

        for (DeviceRequest unit : request) {
            for (Action action : unit.actions) {

                scheduleTumblerJob(unit.deviceID,
                        unit.date.atStartOfDay().plusHours(action.timeOn.getHour()).plusMinutes(action.timeOn.getMinute()).plusSeconds(action.timeOn.getSecond()),
                        Action.ActionType.turnOn);
                scheduleTumblerJob(unit.deviceID,
                        unit.date.atStartOfDay().plusHours(action.timeOff.getHour()).plusMinutes(action.timeOff.getMinute()).plusSeconds(action.timeOff.getSecond()),
                        Action.ActionType.turnOff);
            }
        }
    }

    public void scheduleTumblerJob(String deviceID, LocalDateTime dateTime, Action.ActionType action) throws SchedulerException {
        if (dateTime.isAfter(LocalDateTime.now())) {

            JobDetail job = newJob(TumblerJob.class)
                    .withIdentity(jobNum.toString(), deviceID)
                    .build();

            Trigger trigger = newTrigger()
                    .withIdentity(jobNum.toString(), dateTime.toLocalDate().toString())
                    .startAt(java.sql.Timestamp.valueOf(dateTime))
                    .build();

            job.getJobDataMap().put("deviceID", deviceID);
            job.getJobDataMap().put("dateTime", dateTime);
            job.getJobDataMap().put("action", action);

            jobs.add(job.getKey());
            scheduler.scheduleJob(job, trigger);
            System.out.println("scheduled to " + action.toString() + " " + deviceID + " device at " + dateTime);

            jobNum++;
        } else {
            System.out.println("not scheduled to " + action.toString() + " " + deviceID + " device at " +
                    dateTime + " because of wrong date-time value");
        }
    }
}
package com.byelex.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.byelex.entity.Action;
import com.byelex.job.TumblerJob;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.byelex.component.JobScheduleCreator;


@Service
public class SchedulerJobService {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private JobScheduleCreator scheduleCreator;


    public List<JobDetail> getAllJobList() {
        try {
            return scheduler.getJobKeys(GroupMatcher.anyJobGroup()).stream().map(jk-> {
                try {
                    return scheduler.getJobDetail(jk);
                } catch (SchedulerException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }

    public void deleteAllJobs(){
        try {
            scheduler.deleteJobs(new ArrayList<>(scheduler.getJobKeys(GroupMatcher.anyJobGroup())));
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteJobsByDateAndDeviceId(LocalDate date, String deviceId) {
        System.out.println("deletion of " + deviceId + "|" + date);
        try {
            scheduler.deleteJobs(new ArrayList<>(scheduler.getJobKeys(GroupMatcher.groupEquals(deviceId + "|" + date))));

        } catch (SchedulerException e) {
            System.out.println(e.getMessage());
        }
    }

    public void scheduleNewJob(String deviceID, LocalDate date, LocalTime time, Action.ActionType action) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();

            JobDetail jobDetail = JobBuilder
                    .newJob(TumblerJob.class).build();
            if (!scheduler.checkExists(jobDetail.getKey())) {

                jobDetail = scheduleCreator.createJob(TumblerJob.class, context, deviceID + "|" + date);

                Trigger trigger;

                trigger = scheduleCreator.createSimpleTrigger(java.sql.Timestamp.valueOf(date.atStartOfDay()
                        .plusHours(time.getHour())
                        .plusMinutes(time.getMinute())
                        .plusSeconds(time.getSecond())));

                jobDetail.getJobDataMap().put("action", action.toString());

                scheduler.scheduleJob(jobDetail, trigger);

            }
        } catch (SchedulerException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}

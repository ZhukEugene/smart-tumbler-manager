package com.byelex.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.byelex.job.TumblerJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.byelex.component.JobScheduleCreator;
import com.byelex.entity.SchedulerJobInfo;
import com.byelex.repository.SchedulerRepository;


@Service
public class SchedulerJobService {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private SchedulerRepository schedulerRepository;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private JobScheduleCreator scheduleCreator;


    public List<SchedulerJobInfo> getAllJobList() {
        return schedulerRepository.findAll();
    }

    public void deleteJobsByDateAndDeviceId(LocalDate date, String deviceId) {
        System.out.println("deletion of " + date + " " + deviceId);

        List<SchedulerJobInfo> jobsInfo = schedulerRepository.findAllByJobDateAndJobDeviceId(date, deviceId);
        if (jobsInfo.isEmpty())
            return;
        for (SchedulerJobInfo info : jobsInfo
        ) {
            try {
                scheduler.deleteJob(new JobKey(String.valueOf(info.getJobId()), String.valueOf(info.getJobId())));
                schedulerRepository.delete(info);
            }
            catch (SchedulerException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void scheduleNewJob(SchedulerJobInfo jobInfo) {
        try {

            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            System.out.println("get scheduler");
            JobDetail jobDetail = JobBuilder
                    .newJob(TumblerJob.class).build();
            if (!scheduler.checkExists(jobDetail.getKey())) {

                System.out.println("got into if");
                jobDetail = scheduleCreator.createJob(TumblerJob.class, context,
                        String.valueOf(jobInfo.getJobId()), String.valueOf(jobInfo.getJobId()));

                Trigger trigger;

                trigger = scheduleCreator.createSimpleTrigger(String.valueOf(jobInfo.getJobId()),
                        java.sql.Timestamp.valueOf(jobInfo.getJobDate().atStartOfDay()
                                .plusHours(jobInfo.getJobTime().getHour())
                                .plusMinutes(jobInfo.getJobTime().getMinute())
                                .plusSeconds(jobInfo.getJobTime().getSecond())));
                System.out.println("make trigger");

                jobDetail.getJobDataMap().put("action", jobInfo.getJobActionType());
                jobDetail.getJobDataMap().put("deviceId", jobInfo.getJobDeviceId());

                scheduler.scheduleJob(jobDetail, trigger);
                System.out.println("scheduled");
                schedulerRepository.save(jobInfo);
                System.out.println("saved");

            }
        } catch (SchedulerException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}

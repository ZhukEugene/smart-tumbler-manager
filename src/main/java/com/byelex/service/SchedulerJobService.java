package com.byelex.service;

import java.time.LocalDate;
import java.util.List;

import com.byelex.job.TumblerJob;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
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
        List<SchedulerJobInfo> jobsInfo = schedulerRepository.findAllByJobDateAndJobDeviceId(date, deviceId);
        if(jobsInfo.isEmpty())
            return;
        schedulerRepository.deleteAll(jobsInfo);
    }

    public void scheduleNewJob(SchedulerJobInfo jobInfo) {
        try {

            Scheduler scheduler = schedulerFactoryBean.getScheduler();

            JobDetail jobDetail = JobBuilder
                    .newJob(TumblerJob.class).build();
            if (!scheduler.checkExists(jobDetail.getKey())) {

                jobDetail = scheduleCreator.createJob(TumblerJob.class, context);

                Trigger trigger;

                trigger = scheduleCreator.createSimpleTrigger(jobInfo.getJobId(),
                        java.sql.Timestamp.valueOf(jobInfo.getJobDate().atStartOfDay()
                                .plusHours(jobInfo.getJobTime().getHour())
                                .plusMinutes(jobInfo.getJobTime().getMinute())
                                .plusSeconds(jobInfo.getJobTime().getSecond())));
                scheduler.scheduleJob(jobDetail, trigger);

                schedulerRepository.save(jobInfo);
            }
        } catch (SchedulerException ignored) {

        }
    }
}

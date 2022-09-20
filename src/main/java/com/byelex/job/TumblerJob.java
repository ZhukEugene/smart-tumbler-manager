package com.byelex.job;

import org.quartz.*;

import java.time.LocalDateTime;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class TumblerJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        System.out.println("\nsend " + context.getJobDetail().getJobDataMap().get("action") + " signal to "
                + context.getJobDetail().getJobDataMap().get("deviceID") +
                "\nTimestamp: " + LocalDateTime.now());

    }
}

package com.byelex.manager;

import org.quartz.*;

import java.time.LocalDateTime;

public class TumblerJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        System.out.println("\nsend " + jobExecutionContext.getJobDetail().getJobDataMap().get("action") + " signal to "
                + jobExecutionContext.getJobDetail().getJobDataMap().get("deviceID") +
                "\nTimestamp: " + LocalDateTime.now());

    }
}

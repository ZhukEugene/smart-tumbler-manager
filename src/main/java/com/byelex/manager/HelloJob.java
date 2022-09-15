package com.byelex.manager;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class HelloJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        System.out.println(jobExecutionContext.getJobDetail().getKey() + " working\n"
        + jobExecutionContext.getFireTime() + "\n" + jobExecutionContext.getTrigger().getCalendarName() + "\n"
                );

    }
}

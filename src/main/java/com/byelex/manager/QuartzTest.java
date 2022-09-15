package com.byelex.manager;// file: QuartzTest.java

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class QuartzTest {

    public static void main(String[] args) {

        try {
            // Grab the Scheduler instance from the Factory
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            // and start it off
            scheduler.start();

            for(int j = 0; j < 20; j++) {

                JobDetail job = newJob(HelloJob.class)
                        .withIdentity("job" + j, "group" + j)
                        .build();

                // Trigger the job to run now, and then repeat every 40 seconds
                Trigger trigger = newTrigger()
                        .withIdentity("trigger" + j, "group" + j)
                        .startAt(java.sql.Timestamp.valueOf(LocalDateTime.now().plusSeconds(java.util.concurrent.ThreadLocalRandom.current().nextInt(1, 11))))
                        .build();


                // Tell quartz to schedule the job using our trigger
                scheduler.scheduleJob(job, trigger);
            }
                Thread.sleep(5000);

            for(int j = 0; j < 20; j++) {


            }
            scheduler.shutdown();
        } catch (SchedulerException se) {
            se.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
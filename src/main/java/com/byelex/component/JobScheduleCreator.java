package com.byelex.component;

import java.util.Date;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class JobScheduleCreator {

    private static Long LAST_JOB_ID = 0L;
    private String getNewJobId(){
        return (LAST_JOB_ID++).toString();
    }

    public JobDetail createJob(Class<? extends QuartzJobBean> jobClass, ApplicationContext context) {
        String id = getNewJobId();
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(jobClass);
        factoryBean.setApplicationContext(context);
        factoryBean.setGroup(id);
        factoryBean.setName(id);
        // set job data map
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(id, jobClass.getName());
        factoryBean.setJobDataMap(jobDataMap);
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }


    public SimpleTrigger createSimpleTrigger(String triggerName, Date startTime) {
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setName(triggerName);
        factoryBean.setStartTime(startTime);
        factoryBean.setRepeatCount(1);
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }
}

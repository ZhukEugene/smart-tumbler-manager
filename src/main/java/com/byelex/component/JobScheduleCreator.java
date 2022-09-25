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

    public JobDetail createJob(Class<? extends QuartzJobBean> jobClass, ApplicationContext context, String group) {

        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(jobClass);
        factoryBean.setApplicationContext(context);
        factoryBean.setGroup(group);

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(group, jobClass.getName());

        factoryBean.setJobDataMap(jobDataMap);
        factoryBean.afterPropertiesSet();

        return factoryBean.getObject();
    }


    public SimpleTrigger createSimpleTrigger(Date startTime) {
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setStartTime(startTime);
        factoryBean.setRepeatCount(0);
        factoryBean.setRepeatInterval(1);
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }
}

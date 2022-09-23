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



    public JobDetail createJob(Class<? extends QuartzJobBean> jobClass, ApplicationContext context, String group, String name) {
        System.out.println("got job id");

        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(jobClass);
        factoryBean.setApplicationContext(context);
        factoryBean.setGroup(group);
        factoryBean.setName(name);

        System.out.println("factory Bean OK");

        // set job data map
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(group+"."+name, jobClass.getName());

        System.out.println("Data map ok");

        factoryBean.setJobDataMap(jobDataMap);
        factoryBean.afterPropertiesSet();
        System.out.println("properties set");

        return factoryBean.getObject();
    }


    public SimpleTrigger createSimpleTrigger(String triggerName, Date startTime) {
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setName(triggerName);
        factoryBean.setStartTime(startTime);
        factoryBean.setRepeatCount(1);
        factoryBean.setRepeatInterval(1);
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }
}

package com.byelex.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class SchedulerConfig {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private QuartzProperties quartzProperties;



	@Bean
	public SchedulerFactoryBeanCustomizer schedulerFactoryBeanCustomizer()
	{
		return new SchedulerFactoryBeanCustomizer()
		{
			@Override
			public void customize(SchedulerFactoryBean bean)
			{
				bean.setQuartzProperties(createQuartzProperties());
			}
		};
	}

	private Properties createQuartzProperties()
	{
		// Could also load from a file
		Properties props = new Properties();
		props.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate");
		return props;
	}
	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {

		SchedulerJobFactory jobFactory = new SchedulerJobFactory();
		jobFactory.setApplicationContext(applicationContext);

		Properties properties = new Properties();
		properties.putAll(quartzProperties.getProperties());

		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		//factory.setConfigLocation(new ClassPathResource("quartz.properties"));
		factory.setDataSource(dataSource);
		factory.setQuartzProperties(properties);
		factory.setJobFactory(jobFactory);
		return factory;
	}
}

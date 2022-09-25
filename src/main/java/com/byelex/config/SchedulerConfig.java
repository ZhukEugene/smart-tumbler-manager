package com.byelex.config;

import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class SchedulerConfig {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private ApplicationContext applicationContext;

	private Properties createQuartzProperties() {
		try {
			Properties prop = new Properties();
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream stream = loader.getResourceAsStream("quartz.properties");
			prop.load(stream);
			prop.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate");
			return prop;
		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}
		return null;
	}
	@Bean
	public SchedulerFactoryBean schedulerFactoryBean(){

		SchedulerJobFactory jobFactory = new SchedulerJobFactory();
		jobFactory.setApplicationContext(applicationContext);


		SchedulerFactoryBean factory = new SchedulerFactoryBean();

		factory.setQuartzProperties(createQuartzProperties());

		factory.setDataSource(dataSource);
		factory.setJobFactory(jobFactory);
		return factory;
	}
}

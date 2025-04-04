package kr.co.skcc.base.bap.scheduler.config;

import kr.co.skcc.base.bap.scheduler.component.JobsListener;
import kr.co.skcc.base.bap.scheduler.component.TriggersListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import java.util.Properties;

@Configuration
@Slf4j
public class SchedulerConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${spring.application.name:com-batch}")
    private String appName;

    @Autowired
    private JobsListener jobsListener;

    @Autowired
    private TriggersListener triggersListener;

//    @Autowired
//    @Qualifier("quartzDataSource")
//    private DataSource dataSource;

    @Autowired
    private QuartzProperties quartzProperties;

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

//    @Bean
//    public SchedulerFactoryBean schedulerFactory(@Qualifier("quartzDataSource") DataSource dataSource,
//                                                 @Qualifier("quartzTransactionManager") PlatformTransactionManager transactionManager) {
//        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
//        schedulerFactory.setConfigLocation(new ClassPathResource("quartz.properties.bak"));
//
//        schedulerFactory.setSchedulerName(appName);
//        schedulerFactory.setGlobalJobListeners(jobsListener);
//        schedulerFactory.setGlobalTriggerListeners(triggersListener);
//        schedulerFactory.setWaitForJobsToCompleteOnShutdown(true);
//        schedulerFactory.setOverwriteExistingJobs(true);
//        schedulerFactory.setAutoStartup(true);
//
//        schedulerFactory.setDataSource(dataSource);
//        schedulerFactory.setTransactionManager(transactionManager);
//        schedulerFactory.setJobFactory(springBeanJobFactory());
//
//        return schedulerFactory;
//    }

    @Bean
    public SchedulerFactoryBean schedulerFactory() {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();

        Properties properties = new Properties();
        properties.putAll(quartzProperties.getProperties());

        schedulerFactory.setSchedulerName(appName);
        schedulerFactory.setGlobalJobListeners(jobsListener);
        schedulerFactory.setGlobalTriggerListeners(triggersListener);
        schedulerFactory.setWaitForJobsToCompleteOnShutdown(true);
        schedulerFactory.setOverwriteExistingJobs(true);
        schedulerFactory.setAutoStartup(true);
//        schedulerFactory.setDataSource(dataSource);
        schedulerFactory.setQuartzProperties(properties);
        schedulerFactory.setJobFactory(springBeanJobFactory());

        return schedulerFactory;
    }
}

package kr.co.skcc.oss.bap.scheduler.component;

import kr.co.skcc.oss.bap.scheduler.domain.JobParameter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JobScheduleCreator {

    public JobDetail createJob(String jobName, String jobGroup, String description,
                               Class<? extends QuartzJobBean> jobClass, boolean isDurable, List<JobParameter> jobData) {
        log.debug("JobName={}", jobName);
        log.debug("JobGroup={}", jobGroup);
        log.debug("Description={}", description);
        log.debug("JobClass={}", jobClass.getName());

        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setName(jobName);
        factoryBean.setGroup(jobGroup);
        factoryBean.setDescription(description);
        factoryBean.setJobClass(jobClass);
        factoryBean.setDurability(isDurable);
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobData", jobData);
        factoryBean.setJobDataMap(jobDataMap);

        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }

    public CronTrigger createCronTrigger(String triggerName, String triggerGroup, Date startTime, String cronExpression, int misFireInstruction) {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setName(triggerName);
        factoryBean.setGroup(triggerGroup);
        factoryBean.setStartTime(startTime);
        factoryBean.setCronExpression(cronExpression);
        factoryBean.setMisfireInstruction(misFireInstruction);
        try {
            factoryBean.afterPropertiesSet();
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        return factoryBean.getObject();
    }

    public SimpleTrigger createSimpleTrigger(String triggerName, String triggerGroup, Date startTime, long repeatInterval, int repeatCount, int misFireInstruction) {
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setName(triggerName);
        factoryBean.setGroup(triggerGroup);
        factoryBean.setStartTime(startTime);
        factoryBean.setRepeatInterval(repeatInterval);
        factoryBean.setRepeatCount(repeatCount);
        factoryBean.setMisfireInstruction(misFireInstruction);
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }
}

package kr.co.skcc.base.bap.scheduler.job;

import kr.co.skcc.base.bap.scheduler.config.BatchConfig;
import kr.co.skcc.base.bap.scheduler.domain.JobParameter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

/**
 * 스케줄링 작업
 * 시작일시 기준으로 Cron 설정에 맞춰 반복 실행된다.
 */
@Slf4j
public class CronJob extends QuartzJobBean {

    // @Autowired
    // Scheduler scheduler;

    @Autowired
    private JobLocator jobLocator;

    @Autowired
    private JobLauncher jobLauncher;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            log.debug("SchedulerName={}", context.getScheduler().getSchedulerName());
            log.debug("FireTime={}", context.getFireTime());
            log.debug("ScheduledFireTime={}", context.getScheduledFireTime());
            log.debug("NextFireTime={}", context.getNextFireTime());
            log.debug("PreviousFireTime={}", context.getPreviousFireTime());
            log.debug("JobRunTime={}", context.getJobRunTime());

            JobDetail jobDetail = context.getJobDetail();
            log.debug("JobName={}", jobDetail.getKey().getName());
            log.debug("JobGroup={}", jobDetail.getKey().getGroup());
            log.debug("Description={}", jobDetail.getDescription());
            log.debug("JobClass={}", jobDetail.getJobClass().getName());
            jobDetail.getJobDataMap().forEach((key, value) -> {
                log.debug("JobData: {}={}", key, value);
            });

            Trigger trigger = context.getTrigger();
            log.debug("TriggerName={}", trigger.getKey().getName());
            log.debug("TriggerGroup={}", trigger.getKey().getGroup());
            log.debug("StartTime={}", trigger.getStartTime());
            log.debug("EndTime={}", trigger.getEndTime());
            log.debug("NextFireTime={}", trigger.getNextFireTime());
            log.debug("PreviousFireTime={}", trigger.getPreviousFireTime());
            log.debug("FinalFireTime={}", trigger.getFinalFireTime());
            // log.debug("TriggerState={}", scheduler.getTriggerState(new TriggerKey(trigger.getKey().getName(), trigger.getKey().getGroup())).toString());

            org.springframework.batch.core.Job job = jobLocator.getJob(jobDetail.getKey().getName());
            JobParameters params = BatchConfig.getJobParameters((List<JobParameter>) jobDetail.getJobDataMap().get("jobData"));
            JobExecution jobExecution = jobLauncher.run(job, params);
            log.info("{} : {} was completed successfully.", job.getName(), jobExecution.getId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}

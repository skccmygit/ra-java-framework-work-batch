package kr.co.skcc.base.bap.scheduler.service;

import kr.co.skcc.base.bap.scheduler.component.JobScheduleCreator;
import kr.co.skcc.base.bap.scheduler.domain.SchedulerJobInfo;
import kr.co.skcc.base.bap.scheduler.domain.type.JobInfoStatus;
import kr.co.skcc.base.bap.scheduler.job.CronJob;
import kr.co.skcc.base.bap.scheduler.job.SimpleJob;
import kr.co.skcc.base.bap.scheduler.repository.SchedulerRepository;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * scheduler job service
 */
@Transactional("quartzTransactionManager")
@Service
@Slf4j
public class SchedulerJobService {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private JobScheduleCreator scheduleCreator;

    @Autowired
    private SchedulerRepository schedulerRepository;

    /**
     * 작업 스케줄 등록 or 수정
     */
    public void saveOrUpdate(SchedulerJobInfo jobInfo) throws SchedulerException, ClassNotFoundException {
        if (ObjectUtils.isEmpty(jobInfo.getCronExpression())) { // Cron 표현식 존재여부
            jobInfo.setJobClass(SimpleJob.class.getName());
            jobInfo.setCronJob(false);
        } else {
            jobInfo.setJobClass(CronJob.class.getName());
            jobInfo.setCronJob(true);
        }

        if (ObjectUtils.isEmpty(jobInfo.getStartTime())) { // 시작일시 존재여부
            jobInfo.setStartTime(new Date());
        }

//        SchedulerJobInfo getJobInfo = schedulerRepository.findByJobNameAndJobGroup(jobInfo.getJobName(), jobInfo.getJobGroup());
        SchedulerJobInfo getJobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
        String oldJobGroup = null;
        if (ObjectUtils.isEmpty(getJobInfo)) { // JobName 존재여부
            jobInfo.setJobId(null);
            oldJobGroup = "NONE";
        } else {
            jobInfo.setJobId(getJobInfo.getJobId());
            oldJobGroup = getJobInfo.getJobGroup();
        }
        log.debug("JobId={},JobGroup={},OldJobGroup={}", jobInfo.getJobId(), jobInfo.getJobGroup(), oldJobGroup);

        scheduleJob(jobInfo, oldJobGroup);
    }

    /**
     * 작업 스케줄 등록
     */
    private void scheduleJob(SchedulerJobInfo jobInfo, String oldJobGroup) throws SchedulerException, ClassNotFoundException {
        try {
            JobDetail newJobDetail = scheduleCreator.createJob(jobInfo.getJobName(), jobInfo.getJobGroup(), jobInfo.getDescription(),
                    (Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()), true, jobInfo.getJobParameters());

            Trigger newTrigger;
            if (jobInfo.getCronJob()) {
                newTrigger = scheduleCreator.createCronTrigger(jobInfo.getJobName(), jobInfo.getJobGroup(),
                        jobInfo.getStartTime(), jobInfo.getCronExpression(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
            } else {
                newTrigger = scheduleCreator.createSimpleTrigger(jobInfo.getJobName(), jobInfo.getJobGroup(),
                        jobInfo.getStartTime(), jobInfo.getRepeatInterval(), jobInfo.getRepeatCount(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
            }

            Scheduler scheduler = schedulerFactoryBean.getScheduler();
//            if (!scheduler.checkExists(new JobKey(jobInfo.getJobName(), oldJobGroup))) { // JobKey 존재여부
            if (schedulerRepository.findByJobName(jobInfo.getJobName()) == null) { // JobName 존재여부
                // 신규 Job 정보 및 스케줄 생성
                scheduler.scheduleJob(newJobDetail, newTrigger);
                jobInfo.setJobInfoStatus(JobInfoStatus.SCHEDULED);
                schedulerRepository.save(jobInfo);
                log.info(">>>>> jobKey = {},{} created and scheduled.", jobInfo.getJobName(), jobInfo.getJobGroup());
            } else {
                Set<Trigger> triggersForJob = new HashSet<Trigger>();
                triggersForJob.add(newTrigger);
                // 기존 Job 정보 및 스케줄 갱신
                if (!scheduler.checkExists(TriggerKey.triggerKey(jobInfo.getJobName(), oldJobGroup))) { // Trigger 존재여부
                    scheduler.scheduleJob(newJobDetail, triggersForJob, true);
                    log.debug(">>>>> scheduled Job...");
                } else {
                    scheduler.deleteJob(new JobKey(jobInfo.getJobName(), oldJobGroup));
                    scheduler.scheduleJob(newJobDetail, triggersForJob, true);
//                    scheduler.addJob(newJobDetail, true);
//                    scheduler.rescheduleJob(TriggerKey.triggerKey(jobInfo.getJobName(), oldJobGroup), newTrigger);
                    log.debug(">>>>> rescheduled Job...");
                }
                jobInfo.setJobInfoStatus(JobInfoStatus.RESCHEDULED);
                schedulerRepository.save(jobInfo);
                log.info(">>>>> jobKey = {},{} updated and scheduled.", jobInfo.getJobName(), jobInfo.getJobGroup());
            }
        } catch (ClassNotFoundException e) {
            log.error("Class Not Found - {}", jobInfo.getJobClass(), e);
            throw e;
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 스케줄러 메타정보 조회
     */
    public SchedulerMetaData getMetaData() throws SchedulerException {
        return schedulerFactoryBean.getScheduler().getMetaData();
    }

    /**
     * 작업 목록 조회
     */
    public List<SchedulerJobInfo> getAllJobs() {
        return schedulerRepository.findAll();
    }

    /**
     * 작업 상세 조회
     */
    public SchedulerJobInfo getJobInfo(SchedulerJobInfo jobInfo) {
        return schedulerRepository.findByJobNameAndJobGroup(jobInfo.getJobName(), jobInfo.getJobGroup());
    }

    /**
     * 작업 즉시실행
     */
    public boolean startJob(SchedulerJobInfo jobInfo) throws SchedulerException {
        try {
            SchedulerJobInfo getJobInfo = schedulerRepository.findByJobNameAndJobGroup(jobInfo.getJobName(), jobInfo.getJobGroup());
            getJobInfo.setJobInfoStatus(JobInfoStatus.STARTED);
            schedulerRepository.save(getJobInfo);
            schedulerFactoryBean.getScheduler().triggerJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
            log.info(">>>>> jobKey = {},{} scheduled and started now.", jobInfo.getJobName(), jobInfo.getJobGroup());
            return true;
        } catch (SchedulerException e) {
            log.error("Failed to start new job - {},{}", jobInfo.getJobName(), jobInfo.getJobGroup(), e);
            throw e;
        }
    }

    /**
     * 작업 잠시멈춤
     */
    public boolean pauseJob(SchedulerJobInfo jobInfo) throws SchedulerException {
        try {
            SchedulerJobInfo getJobInfo = schedulerRepository.findByJobNameAndJobGroup(jobInfo.getJobName(), jobInfo.getJobGroup());
            getJobInfo.setJobInfoStatus(JobInfoStatus.PAUSED);
            schedulerRepository.save(getJobInfo);
            schedulerFactoryBean.getScheduler().pauseJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
            log.info(">>>>> jobKey = {},{} paused.", jobInfo.getJobName(), jobInfo.getJobGroup());
            return true;
        } catch (SchedulerException e) {
            log.error("Failed to pause job - {},{}", jobInfo.getJobName(), jobInfo.getJobGroup(), e);
            throw e;
        }
    }

    /**
     * 작업 재시작
     */
    public boolean resumeJob(SchedulerJobInfo jobInfo) throws SchedulerException {
        try {
            SchedulerJobInfo getJobInfo = schedulerRepository.findByJobNameAndJobGroup(jobInfo.getJobName(), jobInfo.getJobGroup());
            getJobInfo.setJobInfoStatus(JobInfoStatus.RESUMED);
            schedulerRepository.save(getJobInfo);
            schedulerFactoryBean.getScheduler().resumeJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
            log.info(">>>>> jobKey = {},{} resumed.", jobInfo.getJobName(), jobInfo.getJobGroup());
            return true;
        } catch (SchedulerException e) {
            log.error("Failed to resume job - {},{}", jobInfo.getJobName(), jobInfo.getJobGroup(), e);
            throw e;
        }
    }

    /**
     * 작업 삭제
     */
    public boolean deleteJob(SchedulerJobInfo jobInfo) throws SchedulerException {
        try {
            SchedulerJobInfo getJobInfo = schedulerRepository.findByJobNameAndJobGroup(jobInfo.getJobName(), jobInfo.getJobGroup());
            schedulerRepository.delete(getJobInfo);
            schedulerFactoryBean.getScheduler().deleteJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));

            // 해당 jobName 관련 Spring Batch 메타데이터 삭제
            schedulerRepository.deleteBatchStepExecutionContext(jobInfo.getJobName());
            schedulerRepository.deleteBatchStepExecution(jobInfo.getJobName());
            schedulerRepository.deleteBatchJobExecutionContext(jobInfo.getJobName());
            schedulerRepository.deleteBatchJobExecutionParams(jobInfo.getJobName());
            schedulerRepository.deleteBatchJobExecution(jobInfo.getJobName());
            schedulerRepository.deleteBatchJobInstance(jobInfo.getJobName());

            log.info(">>>>> jobKey = {},{} deleted.", jobInfo.getJobName(), jobInfo.getJobGroup());
            return true;
        } catch (SchedulerException e) {
            log.error("Failed to delete job - {},{}", jobInfo.getJobName(), jobInfo.getJobGroup(), e);
            throw e;
        }
    }

    /**
     * 스케줄러 Start
     */
    public void start() throws SchedulerException {
        if (schedulerFactoryBean.getScheduler().isShutdown()) {
            schedulerFactoryBean.getScheduler().start();
        } else {
            throw new SchedulerException("Scheduler already started.");
        }
    }

    /**
     * 스케줄러 Shutdown
     */
    public void shutdown(boolean waitForJobsToComplete) throws SchedulerException {
        if (schedulerFactoryBean.getScheduler().isShutdown()) {
            throw new SchedulerException("Scheduler already shutdown.");
        } else {
            schedulerFactoryBean.getScheduler().shutdown(waitForJobsToComplete);
        }
    }
}

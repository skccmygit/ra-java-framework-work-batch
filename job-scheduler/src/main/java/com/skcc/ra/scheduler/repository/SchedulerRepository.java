package com.skcc.ra.scheduler.repository;

import com.skcc.ra.scheduler.domain.SchedulerJobInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SchedulerRepository extends JpaRepository<SchedulerJobInfo, Long> {

    SchedulerJobInfo findByJobName(String jobName);

    SchedulerJobInfo findByJobNameAndJobGroup(String jobName, String jobGroup);

    @Query(value = "DELETE FROM quartz.BATCH_STEP_EXECUTION_CONTEXT WHERE STEP_EXECUTION_ID IN (SELECT STEP_EXECUTION_ID FROM quartz.BATCH_STEP_EXECUTION WHERE JOB_EXECUTION_ID IN (SELECT JOB_EXECUTION_ID FROM quartz.BATCH_JOB_EXECUTION WHERE JOB_INSTANCE_ID IN (SELECT JOB_INSTANCE_ID FROM quartz.BATCH_JOB_INSTANCE WHERE JOB_NAME=:jobName)))", nativeQuery = true)
    @Modifying
    int deleteBatchStepExecutionContext(String jobName);

    @Query(value = "DELETE FROM quartz.BATCH_STEP_EXECUTION WHERE JOB_EXECUTION_ID IN (SELECT JOB_EXECUTION_ID FROM quartz.BATCH_JOB_EXECUTION WHERE JOB_INSTANCE_ID IN (SELECT JOB_INSTANCE_ID FROM quartz.BATCH_JOB_INSTANCE WHERE JOB_NAME=:jobName))", nativeQuery = true)
    @Modifying
    int deleteBatchStepExecution(String jobName);

    @Query(value = "DELETE FROM quartz.BATCH_JOB_EXECUTION_CONTEXT WHERE JOB_EXECUTION_ID IN (SELECT JOB_EXECUTION_ID FROM quartz.BATCH_JOB_EXECUTION WHERE JOB_INSTANCE_ID IN (SELECT JOB_INSTANCE_ID FROM quartz.BATCH_JOB_INSTANCE WHERE JOB_NAME=:jobName))", nativeQuery = true)
    @Modifying
    int deleteBatchJobExecutionContext(String jobName);

    @Query(value = "DELETE FROM quartz.BATCH_JOB_EXECUTION_PARAMS WHERE JOB_EXECUTION_ID IN (SELECT JOB_EXECUTION_ID FROM quartz.BATCH_JOB_EXECUTION WHERE JOB_INSTANCE_ID IN (SELECT JOB_INSTANCE_ID FROM quartz.BATCH_JOB_INSTANCE WHERE JOB_NAME=:jobName))", nativeQuery = true)
    @Modifying
    int deleteBatchJobExecutionParams(String jobName);

    @Query(value = "DELETE FROM quartz.BATCH_JOB_EXECUTION WHERE JOB_INSTANCE_ID IN (SELECT JOB_INSTANCE_ID FROM quartz.BATCH_JOB_INSTANCE WHERE JOB_NAME=:jobName)", nativeQuery = true)
    @Modifying
    int deleteBatchJobExecution(String jobName);

    @Query(value = "DELETE FROM quartz.BATCH_JOB_INSTANCE WHERE JOB_NAME=:jobName", nativeQuery = true)
    @Modifying
    int deleteBatchJobInstance(String jobName);
}

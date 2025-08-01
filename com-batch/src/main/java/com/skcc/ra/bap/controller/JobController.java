package com.skcc.ra.bap.controller;

import com.skcc.ra.scheduler.config.BatchConfig;
import com.skcc.ra.scheduler.domain.JobParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/com-batch")
@Slf4j
public class JobController {

    @Autowired
    @Qualifier("asyncJobLauncher")
    private JobLauncher asyncJobLauncher;

    @Autowired
    JobLocator jobLocator;

    @PostMapping("/{jobName}")
    public BatchStatus run(@PathVariable String jobName, @RequestBody(required = false) List<JobParameter> jobData) throws Exception {
        org.springframework.batch.core.Job job = jobLocator.getJob(jobName);
        JobParameters params = BatchConfig.getJobParameters(jobData);
        JobExecution jobExecution = asyncJobLauncher.run(job, params);
        log.info("JobExecution: {}", jobExecution.getStatus());

        while (jobExecution.isRunning()) {
            log.debug("Job Running");
        }

        return jobExecution.getStatus();
    }

}

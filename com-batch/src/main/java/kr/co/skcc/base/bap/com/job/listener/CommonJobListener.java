package kr.co.skcc.base.bap.com.job.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CommonJobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("beforeJob");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("afterJob");
    }
}

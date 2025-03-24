package kr.co.skcc.base.bap.scheduler.config;

import kr.co.skcc.base.bap.scheduler.domain.JobParameter;
import kr.co.skcc.base.bap.scheduler.domain.type.JobParamType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

@Configuration
@Slf4j
public class BatchConfig {

    private static final String DATE_FORMAT = "yyyyMMdd";
    private static final String TIME_FORMAT = "HHmmss";
    private static final String DATETIME_FORMAT = "yyyyMMddHHmmss";

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;
    }

    public static JobParameters getJobParameters(List<JobParameter> jobData) {
        JobParametersBuilder builder = new JobParametersBuilder();
        log.debug("JobParameters: {}", jobData);
        if (jobData != null) {
            for (JobParameter param : jobData) {
                if (param.getJobParamType().equals(JobParamType.STRING)) {
                    builder.addString(param.getJobParamName(), param.getJobParamValue());
                }
                if (param.getJobParamType().equals(JobParamType.DATE)) {
                    try {
                        SimpleDateFormat formatter = null;
                        if (param.getJobParamValue().length() == 8) formatter = new SimpleDateFormat(DATE_FORMAT);
                        if (param.getJobParamValue().length() == 6) formatter = new SimpleDateFormat(TIME_FORMAT);
                        if (param.getJobParamValue().length() == 14) formatter = new SimpleDateFormat(DATETIME_FORMAT);
                        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
                        builder.addDate(param.getJobParamName(), formatter.parse(param.getJobParamValue()));
                    } catch (ParseException e) {
                        log.error(e.getMessage(), e);
                    }
                }
                if (param.getJobParamType().equals(JobParamType.LONG)) {
                    builder.addLong(param.getJobParamName(), Long.valueOf(param.getJobParamValue()));
                }
                if (param.getJobParamType().equals(JobParamType.DOUBLE)) {
                    builder.addDouble(param.getJobParamName(), Double.valueOf(param.getJobParamValue()));
                }
            }
        }
        builder.addLong("timestamp", System.currentTimeMillis());
        return builder.toJobParameters();
    }
}
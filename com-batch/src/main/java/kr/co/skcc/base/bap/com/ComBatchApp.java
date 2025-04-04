package kr.co.skcc.base.bap.com;

import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@SpringBootApplication(
        exclude = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class
        }
)
@ComponentScan(basePackages = {
        "kr.co.skcc.base.bap.com",
        "kr.co.skcc.base.bap.scheduler",
        "com.adtcaps.nextoss.da.bap.sap"
})
@EnableFeignClients
public class ComBatchApp {

    public static void main(String[] args) {
        // 시스템 속성 설정
        // SysUtils.setSystemProperties();
        SpringApplication.run(ComBatchApp.class, args);
    }

    @Bean(name = "asyncJobLauncher")
    public JobLauncher asyncJobLauncher(JobRepository jobRepository) throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }
}

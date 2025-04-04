package kr.co.skcc.base.bap.com.job;

import jakarta.persistence.EntityManagerFactory;
import kr.co.skcc.base.bap.com.job.listener.CommonJobListener;
import kr.co.skcc.base.bap.com.job.listener.CommonStepListener;
import kr.co.skcc.base.bap.com.job.tasklet.RoleBasedApiUpdateTasklet;
import kr.co.skcc.base.bap.com.job.tasklet.UserBasedApiUpdateTasklet;
import kr.co.skcc.base.bap.com.job.tasklet.WhiteListBasedApiUpdateTasklet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Profile("!test")
@Slf4j
public class ApiAuthorizationInformationConfig {



    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    CommonJobListener commonJobListener;

    @Autowired
    CommonStepListener commonStepListener;

    @Autowired
    EntityManagerFactory entityManagerFactory;

//    @Bean
//    public TaskExecutor taskExecutor() {
//        return new SimpleAsyncTaskExecutor();
//    }

    /*
     * 공통 - 권한관리 - API 권한 리스트 Redis 연동 배치
     * 항목ID : COMPM00001
     * 항목명 : API 권한 리스트 Redis 연동 배치 Job
     * 내용   : API 기준 권한 리스트를 Redis 연동하는 배치
     * */
    @Bean
    public Job ApiAuthorizationInformation() {
        return new JobBuilder("ApiAuthorizationInformationJob", jobRepository)
                .preventRestart()
                .start(whiteListBasedApiUpdateStep())
                .next(roleBasedApiUpdateStep())
                .next(userBasedApiUpdateStep())
                .listener(commonJobListener)
                .build();
    }

    /*
     * 공통 - 권한관리 - API 권한 리스트 Redis 연동 배치
     * 항목ID : COMPM00002
     * 항목명 : API리스트에 등록되어 있는 모든 API에 대하여 Redis 등록 처리 Step
     * 내용   : ONM에 등록된 전체 API리스트를 Redis에 저장
     * */
    @Bean
    @JobScope
    protected Step whiteListBasedApiUpdateStep() {
        return new StepBuilder("whiteListBasedApiUpdateStep", jobRepository)
                .tasklet(whiteListBasedApiUpdateTasklet(), transactionManager)
//                .taskExecutor(taskExecutor())
                // .throttleLimit(2)
                .build();
    }

    /*
     * 공통 - 권한관리 - API 권한 리스트 Redis 연동 배치
     * 항목ID : COMPM00003
     * 항목명 : 역할 기준으로 매핑되어 있는 API에 대하여 Redis에 등록 처리 Step
     * 내용   : 역할에 매핑되어 있는 API리스트를 Redis에 저장(역할별 사용 가능한 API 리스트 저장)
     * */
    @Bean
    public Step roleBasedApiUpdateStep() {
        return new StepBuilder("roleBasedApiUpdateStep", jobRepository)
                .tasklet(roleBasedApiUpdateTasklet(), transactionManager)
//                .taskExecutor(taskExecutor())
                // .throttleLimit(2)
                .build();
    }

    /*
     * 공통 - 권한관리 - API 권한 리스트 Redis 연동 배치
     * 항목ID : COMPM00004
     * 항목명 : 사용자 추가권한기준으로 매핑되어 있는 API에 대하여 Redis에 등록 처리 Step
     * 내용   : 사용자 추가권한에 매핑 되어 있는 API리스트를 Redis 에 저장(사용자별 추가권한에 의해 사용 가능한 API리스트 저장)
     * */
    @Bean
    public Step userBasedApiUpdateStep() {
        return new StepBuilder("userBasedApiUpdateStep", jobRepository)
                .tasklet(userBasedApiUpdateTasklet(), transactionManager)
//                .taskExecutor(taskExecutor())
                // .throttleLimit(2)
                .build();
    }

    @Bean
    public WhiteListBasedApiUpdateTasklet whiteListBasedApiUpdateTasklet() { return new WhiteListBasedApiUpdateTasklet(); }

    @Bean
    public RoleBasedApiUpdateTasklet roleBasedApiUpdateTasklet() {
        return new RoleBasedApiUpdateTasklet();
    }

    @Bean
    public UserBasedApiUpdateTasklet userBasedApiUpdateTasklet() {
        return new UserBasedApiUpdateTasklet();
    }
}

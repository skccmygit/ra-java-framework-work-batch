package com.skcc.ra.bap.job;

import jakarta.persistence.EntityManagerFactory;
import com.skcc.ra.bap.job.listener.CommonJobListener;
import com.skcc.ra.bap.job.listener.CommonStepListener;
import com.skcc.ra.bap.job.processor.AgentEndItemProcessor;
import com.skcc.ra.bap.job.processor.AgentStartItemProcessor;
import com.skcc.ra.bap.job.reader.AgentEndItemReader;
import com.skcc.ra.bap.job.reader.AgentStartItemReader;
import com.skcc.ra.bap.job.writer.AgentItemWriter;
import com.skcc.ra.bap.repository.AccountRepository;
import com.skcc.ra.bap.repository.DeptRepository;
import com.skcc.ra.bap.repository.UserRoleHistRepository;
import com.skcc.ra.bap.repository.UserRoleRepository;
import com.skcc.ra.account.domain.auth.Agent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
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
public class AgentManagementConfig {

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

    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    UserRoleHistRepository userRoleHistRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    DeptRepository deptRepository;

    private final static int CHUNK_SIZE = 5;
    /*
     * 공통 - 대무자관리 - 기간만료된 대무자 종료처리 & 권한 삭제
     * */
    @Bean
    public Job AgentManagementJob() {
        return new JobBuilder("AgentManagementJob", jobRepository)
                .preventRestart()
                .start(agentEnd())
                .next(agentStart())
                .listener(commonJobListener)
                .build();
    }

    /*
     * 공통 - 대무자관리 - 기간만료된 대무자권한 삭제 & 종료처리
     * 1. 만료된 대무자 리스트 추출
     * 2. 대무자 권한 제거
     * 3. 대무 종료처리
     * */
    @Bean
    public Step agentEnd() {
        return new StepBuilder("agentEnd", jobRepository)
                .<Agent, Agent>chunk(CHUNK_SIZE, transactionManager)
                .reader(new AgentEndItemReader(entityManagerFactory))
                .processor(new AgentEndItemProcessor(userRoleRepository,
                                                     userRoleHistRepository,
                        accountRepository,
                                                     deptRepository))
                .writer(new AgentItemWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }

    /*
     * 공통 - 대무자관리 - 기간도래한 대무자권한 부여
     * 1. 만료된 대무자 리스트 추출
     * 2. 대무자 권한 부여
     * */
    @Bean
    public Step agentStart() {
        return new StepBuilder("agentStart", jobRepository)
                .<Agent, Agent>chunk(CHUNK_SIZE, transactionManager)
                .reader(new AgentStartItemReader(entityManagerFactory))
                .processor(new AgentStartItemProcessor(userRoleRepository,
                                                       userRoleHistRepository,
                        accountRepository,
                                                       deptRepository))
                .writer(new AgentItemWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }


}
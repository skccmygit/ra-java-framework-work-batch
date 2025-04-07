package com.skcc.ra.bap.job;

import jakarta.persistence.EntityManagerFactory;
import com.skcc.ra.bap.job.listener.CommonJobListener;
import com.skcc.ra.bap.job.listener.CommonStepListener;
import com.skcc.ra.bap.job.processor.MenuStatisticsItemProcessor;
import com.skcc.ra.bap.job.reader.MenuStatisticsItemReader;
import com.skcc.ra.bap.job.writer.MenuStatisticsItemWriter;
import com.skcc.ra.account.domain.hist.UserActvyLog;
import com.skcc.ra.common.domain.menu.MenuStatistics;
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
public class MenuDailyStatisticsConfig {

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

    private final static int CHUNK_SIZE = 5;

    /*
     * 공통 - 권한관리 - 메뉴접속통계 배치
     * 항목ID : COMMM00001
     * 항목명 : 메뉴접속통계 배치 Job
     * 내용   : 메뉴별 접속 수량 집계 배치
     * */
    @Bean
    public Job menuDailyStatisticsJob() {
        return new JobBuilder("menuDailyStatisticsJob", jobRepository)
                .preventRestart()
                .start(menuDailyStatisticsStep())
                .listener(commonJobListener)
                .build();
    }

    /*
     * 공통 - 권한관리 - 메뉴접속통계 배치
     * 항목ID : COMMM00002
     * 항목명 : 이전날짜 기준 메뉴 접속 이력 통계 저장 Step
     * 내용   : 이전 날짜 기준으로 각 메뉴별 접속 숫자를 집계
     * */
    @Bean
    @JobScope
    public Step menuDailyStatisticsStep() {
        return new StepBuilder("menuDailyStatisticsStep", jobRepository)
                .<UserActvyLog, MenuStatistics>chunk(CHUNK_SIZE, transactionManager)
                .reader(new MenuStatisticsItemReader(entityManagerFactory))
                .processor(new MenuStatisticsItemProcessor())
                .writer(new MenuStatisticsItemWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }
}

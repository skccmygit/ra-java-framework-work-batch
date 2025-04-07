package com.skcc.ra.bap.job;

import com.skcc.ra.bap.job.processor.*;
import com.skcc.ra.bap.job.reader.*;
import jakarta.persistence.EntityManagerFactory;
import com.skcc.ra.bap.job.listener.CommonJobListener;
import com.skcc.ra.bap.job.listener.CommonStepListener;
import com.skcc.ra.bap.job.processor.*;
import com.skcc.ra.bap.job.reader.*;
import com.skcc.ra.bap.job.tasklet.UpdateBssInfoChkTasklet;
import com.skcc.ra.bap.job.writer.BssmacdItemWriter;
import com.skcc.ra.bap.job.writer.DeptItemWriter;
import com.skcc.ra.bap.job.writer.UserBasicItemWriter;
import com.skcc.ra.bap.repository.BssmacdRepository;
import com.skcc.ra.bap.repository.DeptRepository;
import com.skcc.ra.bap.repository.UserBasicRepository;
import com.skcc.ra.common.domain.dept.Bssmacd;
import com.skcc.ra.common.domain.dept.BssmacdTemp;
import com.skcc.ra.common.domain.dept.Dept;
import com.skcc.ra.common.domain.dept.DeptTemp;
import com.skcc.ra.common.domain.userBasic.UserBasic;
import com.skcc.ra.common.domain.userBasic.UserBasicTemp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
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
public class UpdateBssInformConfig {

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
    UserBasicRepository userBasicRepository;

    @Autowired
    BssmacdRepository bssmacdRepository;

    @Autowired
    DeptRepository deptRepository;

    private final static int CHUNK_SIZE = 500;

    /*
    * 공통 - 계정관리 - 인사정보기준 Argos 인사마스터 업데이트 배치
    * 항목ID : COMUM00001
    * 항목명 : 인사정보기준 Argos 인사마스터 업데이트 배치 Job
    * 내용   : SAP 에서 연동받은 인사정보를 연동테이블에서 본테이블로 Merge / Delete 처리하는 배치
    * */
    @Bean
    public Job updateBssInformJob() {
        return new JobBuilder("updateBssInformJob", jobRepository)
                .preventRestart()
                .start(userBasicFlow())
                .next(bssmacdFlow())
                .next(deptFlow())
                .end()
                .listener(commonJobListener)
                .build();

    }

    /*
     * 공통 - 계정관리 - 인사정보기준 Argos 인사마스터 업데이트 배치
     * 항목ID : COMUM00002
     * 항목명 : 사원정보 업데이트 Flow
     * 내용   : SAP에서 연동받은 인사정보 중 사원정보를 연동테이블에서 본테이블로 Merge / Delete 처리
     * */
    private Flow userBasicFlow() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("userBasicFlow");

        flowBuilder.start(mergeUserBasicStep())
                .next(updateUserBasicChkStep()).on("FAILED").end()
                .from(updateUserBasicChkStep()).on("*").to(deleteUserBasicStep()).on("*").end()
                .end();

        return flowBuilder.build();
    }

    /*
     * 공통 - 계정관리 - 인사정보기준 Argos 인사마스터 업데이트 배치
     * 항목ID : COMUM00022
     * 항목명 : 본부코드정보 업데이트 Flow
     * 내용   : SAP에서 연동받은 인사정보 중 본부코드정보를 연동테이블에서 본테이블로 Merge / Delete 처리
     * */
    private Flow bssmacdFlow() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("bssmacdFlow");

        flowBuilder.start(mergeBssmacdStep())
                .next(updateBssmacdChkStep()).on("FAILED").end()
                .from(updateBssmacdChkStep()).on("*").to(deleteBssmacdStep()).on("*").end()
                .end();

        return flowBuilder.build();
    }

    /*
     * 공통 - 계정관리 - 인사정보기준 Argos 인사마스터 업데이트 배치
     * 항목ID : COMUM00004
     * 항목명 : 부서정보 업데이트 Flow
     * 내용   : SAP에서 연동받은 인사정보 중 부서정보를 연동테이블에서 본테이블로 Merge / Delete 처리
     * */
    private Flow deptFlow() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("deptFlow");

        flowBuilder.start(mergeDeptStep())
                .next(updateDeptChkStep()).on("FAILED").end()
                .from(updateDeptChkStep()).on("*").to(deleteDeptStep()).on("*").end()
                .end();

        return flowBuilder.build();
    }



    /*
     * 공통 - 계정관리 - 인사정보기준 Argos 인사마스터 업데이트 배치
     * 항목ID : COMUM00007
     * 항목명 : 사원정보 Merge Step
     * 내용   : 사원정보를 연동테이블에서 본테이블로 Merge
     * */
    @Bean
    @JobScope
    public Step mergeUserBasicStep() {
        return new StepBuilder("mergeUserBasicStep", jobRepository)
                .<UserBasicTemp, UserBasic>chunk(CHUNK_SIZE, transactionManager)
                .reader(new MergeUserBasicItemReader(entityManagerFactory))
                .processor(new MergeUserBasicItemProcessor())
                .writer(new UserBasicItemWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }

    /*
     * 공통 - 계정관리 - 인사정보기준 Argos 인사마스터 업데이트 배치
     * 항목ID : COMUM00008
     * 항목명 : 변경된 사원정보 기준 Delete 할지 여부 체크 Step
     * 내용   : 변경된 사원정보 기준 임계치 이상 변경되지 않았을 경우에만 계정 Delete 하도록 체크하는 Merge(연동 오류로 인한 데이터 손실 방지)
     * */
    @Bean
    public Step updateUserBasicChkStep() {
        return new StepBuilder("updateUserBasicChkStep", jobRepository)
                .tasklet(new UpdateBssInfoChkTasklet("userBasic", userBasicRepository, bssmacdRepository, deptRepository), transactionManager)
                .listener(commonStepListener).build();
    }

    /*
     * 공통 - 계정관리 - 인사정보기준 Argos 인사마스터 업데이트 배치
     * 항목ID : COMUM00009
     * 항목명 : 사원 정보 중 변경되지 않은 정보 삭제 Step
     * 내용   : 사원 정보 중 변경일자가 오늘이 아닌 데이터 삭제
     * */
    @Bean
    @JobScope
    public Step deleteUserBasicStep() {
        return new StepBuilder("deleteUserBasicStep", jobRepository)
                .<UserBasic, UserBasic>chunk(CHUNK_SIZE, transactionManager)
                .reader(new DeleteUserBasicItemReader(entityManagerFactory))
                .processor(new DeleteUserBasicItemProcessor(userBasicRepository))
                .writer(new UserBasicItemWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }

    /*
     * 공통 - 계정관리 - 인사정보기준 Argos 인사마스터 업데이트 배치
     * 항목ID : COMUM00023
     * 항목명 : 본부코드정보 Merge Step
     * 내용   : 본부코드정보를 연동테이블에서 본테이블로 Merge
     * */
    @Bean
    @JobScope
    public Step mergeBssmacdStep() {
        return new StepBuilder("mergeBssmacdStep", jobRepository)
                .<BssmacdTemp, Bssmacd>chunk(CHUNK_SIZE, transactionManager)
                .reader(new MergeBssmacdItemReader(entityManagerFactory))
                .processor(new MergeBssmacdItemProcessor())
                .writer(new BssmacdItemWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }

    /*
     * 공통 - 계정관리 - 인사정보기준 Argos 인사마스터 업데이트 배치
     * 항목ID : COMUM00024
     * 항목명 : 변경된 본부코드정보 기준 Delete 할지 여부 체크 Step
     * 내용   : 변경된 본부코드정보 기준 임계치 이상 변경되지 않았을 경우에만 계정 Delete 하도록 체크하는 Merge(연동 오류로 인한 데이터 손실 방지)
     * */
    @Bean
    public Step updateBssmacdChkStep() {
        return new StepBuilder("updateBssmacdChkStep", jobRepository)
                .tasklet(new UpdateBssInfoChkTasklet("bssmacd", userBasicRepository, bssmacdRepository, deptRepository), transactionManager)
                .listener(commonStepListener).build();
    }

    /*
     * 공통 - 계정관리 - 인사정보기준 Argos 인사마스터 업데이트 배치
     * 항목ID : COMUM00025
     * 항목명 : 본부코드정보 정보 중 변경되지 않은 정보 삭제 Step
     * 내용   : 본부코드정보 중 변경일자가 오늘이 아닌 데이터 삭제
     * */
    @Bean
    @JobScope
    public Step deleteBssmacdStep() {
        return new StepBuilder("deleteBssmacdStep", jobRepository)
                .<Bssmacd, Bssmacd>chunk(CHUNK_SIZE, transactionManager)
                .reader(new DeleteBssmacdItemReader(entityManagerFactory))
                .processor(new DeleteBssmacdItemProcessor(bssmacdRepository))
                .writer(new BssmacdItemWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }

    /*
     * 공통 - 계정관리 - 인사정보기준 Argos 인사마스터 업데이트 배치
     * 항목ID : COMUM00013
     * 항목명 : 부서정보 Merge Step
     * 내용   : 부서정보를 연동테이블에서 본테이블로 Merge
     * */
    @Bean
    @JobScope
    public Step mergeDeptStep() {
        return new StepBuilder("mergeDeptStep", jobRepository)
                .<DeptTemp, Dept>chunk(CHUNK_SIZE, transactionManager)
                .reader(new MergeDeptItemReader(entityManagerFactory))
                .processor(new MergeDeptItemProcessor())
                .writer(new DeptItemWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }

    /*
     * 공통 - 계정관리 - 인사정보기준 Argos 인사마스터 업데이트 배치
     * 항목ID : COMUM00014
     * 항목명 : 변경된 부서정보 기준 Delete 할지 여부 체크 Step
     * 내용   : 변경된 부서정보 기준 임계치 이상 변경되지 않았을 경우에만 계정 Delete 하도록 체크하는 Merge(연동 오류로 인한 데이터 손실 방지)
     * */
    @Bean
    public Step updateDeptChkStep() {
        return new StepBuilder("updateDeptChkStep", jobRepository)
                .tasklet(new UpdateBssInfoChkTasklet("dept", userBasicRepository, bssmacdRepository, deptRepository), transactionManager)
                .listener(commonStepListener).build();
    }

    /*
     * 공통 - 계정관리 - 인사정보기준 Argos 인사마스터 업데이트 배치
     * 항목ID : COMUM00015
     * 항목명 : 부서정보 정보 중 변경되지 않은 정보 삭제 Step
     * 내용   : 부서정보 중 변경일자가 오늘이 아닌 데이터 삭제
     * */
    @Bean
    @JobScope
    public Step deleteDeptStep() {
        return new StepBuilder("deleteDeptStep", jobRepository)
                .<Dept, Dept>chunk(CHUNK_SIZE, transactionManager)
                .reader(new DeleteDeptItemReader(entityManagerFactory))
                .processor(new DeleteDeptItemProcessor(deptRepository))
                .writer(new DeptItemWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }
}

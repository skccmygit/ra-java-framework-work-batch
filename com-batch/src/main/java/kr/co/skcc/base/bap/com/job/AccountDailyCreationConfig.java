package kr.co.skcc.base.bap.com.job;

import jakarta.persistence.EntityManagerFactory;
import kr.co.skcc.base.bap.com.job.listener.CommonJobListener;
import kr.co.skcc.base.bap.com.job.listener.CommonStepListener;
import kr.co.skcc.base.bap.com.job.processor.*;
import kr.co.skcc.base.bap.com.job.reader.AccountCreationItemReader;
import kr.co.skcc.base.bap.com.job.reader.AccountStsCreationItemReader;
import kr.co.skcc.base.bap.com.job.reader.AccountStsCreationReItemReader;
import kr.co.skcc.base.bap.com.job.reader.CapAccountCreationItemReader;
import kr.co.skcc.base.bap.com.job.writer.AccountItemWriter;
import kr.co.skcc.base.bap.com.job.writer.AccountStsChngItemWriter;
import kr.co.skcc.base.bap.com.job.writer.UserRoleHistItemListWriter;
import kr.co.skcc.base.bap.com.job.writer.UserRoleItemListWriter;
import kr.co.skcc.base.bap.com.repository.AccountRepository;
import kr.co.skcc.base.bap.com.repository.DeptRepository;
import kr.co.skcc.base.bap.com.repository.UserRoleDeptMappingRepository;
import kr.co.skcc.base.bap.com.repository.UserRoleRepository;
import kr.co.skcc.base.com.account.domain.account.Account;
import kr.co.skcc.base.com.account.domain.auth.UserRole;
import kr.co.skcc.base.com.account.domain.hist.AccountStsChng;
import kr.co.skcc.base.com.account.domain.hist.UserRoleHist;
import kr.co.skcc.base.com.common.domain.userBasic.UserBasic;
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

import java.util.List;


@Configuration
@Profile("!test")
@Slf4j
public class AccountDailyCreationConfig {

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
    AccountRepository accountRepository;

    @Autowired
    UserRoleDeptMappingRepository userRoleDeptMappingRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    DeptRepository deptRepository;

    private final static int CHUNK_SIZE = 5;


    /*
     * 공통 - 계정관리 - 인사정보 기준 계정 자동 생성 배치
     * 항목ID : COMUM00022
     * 항목명 : 인사정보 기준 계정 자동 생성 배치 Job
     * 내용   : 자동으로 생성되는 계정들에 대한 처리를 담당하는 배치
     * */
    @Bean
    public Job accountDailyCreationJob() {
        return new JobBuilder("accountDailyCreationJob", jobRepository)
                .preventRestart()
                .start(accountDailyCreationStep())
                .next(capAccountDailyCreationStep())
                .next(accountDailyCreationLogStep())
                .next(accountDailyMakeRoleStep())
                .next(accountDailyMakeRoleLogStep())
                .next(accountDailyCreationFinalStep())
                .build();
    }

    /*
     * 공통 - 계정관리 - 인사정보 기준 계정 자동 생성 배치
     * 항목ID : COMUM00023
     * 항목명 : 신규 계정 생성 Step
     * 내용   : 인사정보에 신규로 등록된 계정 중 자동 생성 대상(부서/직책 기준)인 계정에 대하여 계정 생성
     * */
    @Bean
    public Step accountDailyCreationStep() {

        return new StepBuilder("accountDailyCreationStep", jobRepository)
                .<UserBasic, Account>chunk(CHUNK_SIZE, transactionManager)
                .reader(new AccountCreationItemReader(entityManagerFactory))
                .processor(new AccountCreationItemProcessor())
                .writer(new AccountItemWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }

    /*
     * 공통 - 계정관리 - 인사정보 기준 계정 자동 생성 배치
     * 항목ID : COMUM00024
     * 항목명 : 검토자 계정 계정 생성 Step
     * 내용   : 신규로 계정이 생성된 구성원 중 검토자(팀장/담당) 대상자 신규 계정 생성
     * */
    @Bean
    public Step capAccountDailyCreationStep() {
        return new StepBuilder("capAccountDailyCreationStep", jobRepository)
                .<UserBasic, Account>chunk(CHUNK_SIZE, transactionManager)
                .reader(new CapAccountCreationItemReader(entityManagerFactory))
                .processor(new CapAccountCreationItemProcessor())
                .writer(new AccountItemWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }

    /*
     * 공통 - 계정관리 - 인사정보 기준 계정 자동 생성 배치
     * 항목ID : COMUM00025
     * 항목명 : 계정 상태값 변경 이력 저장 Step
     * 내용   : 신규 생성된 계정들에 대하여 상태값 변경 이력을 저장
     * */
    @Bean
    public Step accountDailyCreationLogStep() {
        return new StepBuilder("accountDailyCreationLogStep", jobRepository)
                .<Account, AccountStsChng>chunk(CHUNK_SIZE, transactionManager)
                .reader(new AccountStsCreationItemReader(entityManagerFactory))
                .processor(new AccountStsLogItemProcessor())
                .writer(new AccountStsChngItemWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }

    /*
     * 공통 - 계정관리 - 인사정보 기준 계정 자동 생성 배치
     * 항목ID : COMUM00026
     * 항목명 : 생성된 계정 기본 역할 설정 Step
     * 내용   : 신규 생성된 계정들에 대하여 부서/직책을 기준으로 기 지정된 역할이 부여되며, 검토자는 별도로 검토자 권한을 설정
     * */
    @Bean
    public Step accountDailyMakeRoleStep() {
        return new StepBuilder("accountDailyMakeRoleStep", jobRepository)
                .<Account, List<UserRole>>chunk(CHUNK_SIZE, transactionManager)
                .reader(new AccountStsCreationItemReader(entityManagerFactory))
                .processor(new UserRoleInitItemProcessor(userRoleDeptMappingRepository))
                .writer(new UserRoleItemListWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }

    /*
     * 공통 - 계정관리 - 인사정보 기준 계정 자동 생성 배치
     * 항목ID : COMUM00027
     * 항목명 : 역할 변경 이력 저장 Step
     * 내용   : COMUM00026 에서 설정한 역할에 대한 변경 이력을 저장
     * */
    @Bean
    public Step accountDailyMakeRoleLogStep() {
        return new StepBuilder("accountDailyMakeRoleLogStep", jobRepository)
                .<Account, List<UserRoleHist>>chunk(CHUNK_SIZE, transactionManager)
                .reader(new AccountStsCreationItemReader(entityManagerFactory))
                .processor(new UserRoleInitLogItemProcessor(userRoleRepository, deptRepository))
                .writer(new UserRoleHistItemListWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }

    /*
     * 공통 - 계정관리 - 인사정보 기준 계정 자동 생성 배치
     * 항목ID : COMUM00028
     * 항목명 : 신규 계정 상태값 업데이트 Step
     * 내용   : 신규 계정들에 대해 사용 가능한 상태로 상태값을 최종 업데이트
     * */
    @Bean
    public Step accountDailyCreationFinalStep() {
            return new StepBuilder("accountDailyCreationFinalStep", jobRepository)
                    .<Account, Account>chunk(CHUNK_SIZE, transactionManager)
                    .reader(new AccountStsCreationReItemReader(entityManagerFactory))
                    .processor(new AccountStsTempItemProcessor(accountRepository))
                    .writer(new AccountItemWriter(entityManagerFactory))
                    .listener(commonStepListener)
                    .build();
    }
}
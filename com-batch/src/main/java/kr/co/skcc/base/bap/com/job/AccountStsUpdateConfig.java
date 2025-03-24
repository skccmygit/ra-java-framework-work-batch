package kr.co.skcc.base.bap.com.job;

import jakarta.persistence.EntityManagerFactory;
import kr.co.skcc.base.bap.com.job.listener.CommonJobListener;
import kr.co.skcc.base.bap.com.job.listener.CommonStepListener;
import kr.co.skcc.base.bap.com.job.processor.*;
import kr.co.skcc.base.bap.com.job.reader.*;
import kr.co.skcc.base.bap.com.repository.*;
import kr.co.skcc.base.bap.com.job.processor.*;
import kr.co.skcc.base.bap.com.job.reader.*;
import kr.co.skcc.base.bap.com.job.writer.AccountItemWriter;
import kr.co.skcc.base.bap.com.job.writer.AccountStsChngItemWriter;
import kr.co.skcc.base.bap.com.job.writer.UserRoleHistItemListWriter;
import kr.co.skcc.base.bap.com.repository.*;
import kr.co.skcc.base.com.account.domain.account.Account;
import kr.co.skcc.base.com.account.domain.hist.AccountStsChng;
import kr.co.skcc.base.com.account.domain.hist.UserRoleHist;
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
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@Slf4j
public class AccountStsUpdateConfig {

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
    UserRoleRepository userRoleRepository;

    @Autowired
    UserMenuRepository userMenuRepository;

    @Autowired
    UserScrenBttnRepository userScrenBttnRepository;

    @Autowired
    ShortcutMenuRepository shortcutMenuRepository;

    @Autowired
    BookmarkMenuRepository bookmarkMenuRepository;

    @Autowired
    MyViewDtlRepository myViewDtlRepository;

    @Autowired
    MyViewRepository myViewRepository;

    @Autowired
    UserMenuHistRepository userMenuHistRepository;

    @Autowired
    UserScrenBttnHistRepository userScrenBttnHistRepository;

    private final static int CHUNK_SIZE = 5;

    /*
     * 공통 - 계정관리 - 계정 상태 업데이트(퇴사/비밀번호변경/휴면) 배치
     * 항목ID : COMUM00050
     * 항목명 : 인사정보 기준 계정 상태 업데이트(퇴사/휴면 등) 배치 Job
     * 내용   : 계정상태 변경이 필요한 건들에 대한 처리 배치(퇴사/비밀번호만료/휴면)
     * */
    @Bean
    public Job updateAccountStatusJob() {
        return new JobBuilder("updateAccountStatusJob", jobRepository)
                .preventRestart()
                .start(updateRetireUserStep())
                .next(accountRetireUserLogStep())
                .next(accountRetireUserRoleLogStep())
                .next(accountRetireFinalStep())
                .next(updatePsswdExpiredUserStep())
                .next(accountExpireUserLogStep())
                .next(accountExpireFinalStep())
                .next(updateDormantUserStep())
                .next(accountDormantUserLogStep())
                .next(accountDormantFinalStep())
                .listener(commonJobListener)
                .build();
    }

    /*
     * 공통 - 계정관리 - 계정 상태 업데이트(퇴사/비밀번호변경/휴면) 배치
     * 항목ID : COMUM00051
     * 항목명 : 퇴사자 기본 정보 삭제 처리 Step
     * 내용   : 퇴사자(정직원) 계정 삭제 처리
     * */
    @Bean
    @JobScope
    public Step updateRetireUserStep() {
        return new StepBuilder("updateRetireUserStep", jobRepository)
                .<Account, Account>chunk(CHUNK_SIZE, transactionManager)
                .reader(new RetireUserItemReader(entityManagerFactory))
                .processor(new RetireUserItemProcessor())
                .writer(new AccountItemWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }

    /*
     * 공통 - 계정관리 - 계정 상태 업데이트(퇴사/비밀번호변경/휴면) 배치
     * 항목ID : COMUM00052
     * 항목명 : 퇴사자 기본 정보 삭제 이력 저장 Step
     * 내용   : 퇴사자(정직원) 계정 삭제 처리 이력 저장
     * */
    @Bean
    public Step accountRetireUserLogStep() {
        return new StepBuilder("accountRetireUserLogStep", jobRepository)
                .<Account, AccountStsChng>chunk(CHUNK_SIZE, transactionManager)
                .reader(new AccountRetireItemReader(entityManagerFactory))
                .processor(new AccountRetireLogItemProcessor())
                .writer(new AccountStsChngItemWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }

    /*
     * 공통 - 계정관리 - 계정 상태 업데이트(퇴사/비밀번호변경/휴면) 배치
     * 항목ID : COMUM00053
     * 항목명 : 퇴사자 역할 삭제 이력 저장 Step
     * 내용   : 퇴사자에 대한 역할 삭제 이력 저장
     * */
    @Bean
    public Step accountRetireUserRoleLogStep() {
        return new StepBuilder("accountRetireUserRoleLogStep", jobRepository)
                .<Account, List<UserRoleHist>>chunk(CHUNK_SIZE, transactionManager)
                .reader(new AccountRetireItemReader(entityManagerFactory))
                .processor(new AccountRetireUserRoleLogItemProcessor(userRoleRepository, accountRepository))
                .writer(new UserRoleHistItemListWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }

    /*
     * 공통 - 계정관리 - 계정 상태 업데이트(퇴사/비밀번호변경/휴면) 배치
     * 항목ID : COMUM00054
     * 항목명 : 퇴사자 계정 상태 삭제 Flag 처리 및 역할, 개인화 데이터 삭제 Step
     * 내용   : 퇴사자 계정 상태를 삭제 Flag 처리 및 역할, 추가권한, 바로가기, 즐겨찾기, Cti, 마이메모, 마이뷰 데이터를 삭제
     * */
    @Bean
    public Step accountRetireFinalStep() {
        return new StepBuilder("accountRetireFinalStep", jobRepository)
                .<Account, Account>chunk(CHUNK_SIZE, transactionManager)
                .reader(new AccountRetireReItemReader(entityManagerFactory))
                .processor(new AccountRetireTempItemProcessor(accountRepository,
                                                              userRoleRepository,
                                                              userMenuRepository,
                                                              userScrenBttnRepository,
                                                              shortcutMenuRepository,
                                                              bookmarkMenuRepository,
                                                              myViewRepository,
                                                              myViewDtlRepository,
                                                              userMenuHistRepository,
                                                              userScrenBttnHistRepository))
                .writer(new AccountItemWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }

    /*
     * 공통 - 계정관리 - 계정 상태 업데이트(퇴사/비밀번호변경/휴면) 배치
     * 항목ID : COMUM00055
     * 항목명 : 비밀번호 만료자(상담사) 추출 Step
     * 내용   : 비밀번호가 만료된 상담사 추출(기준 : 비밀번호 변경한지 3개월 초과)
     * */
    @Bean
    @JobScope
    public Step updatePsswdExpiredUserStep() {
        return new StepBuilder("updatePsswdExpiredUserStep", jobRepository)
                .<Account, Account>chunk(CHUNK_SIZE, transactionManager)
                .reader(new PsswdExpireUserItemReader(entityManagerFactory))
                .processor(new PsswdExpiredUserItemProcessor())
                .writer(new AccountItemWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }

    /*
     * 공통 - 계정관리 - 계정 상태 업데이트(퇴사/비밀번호변경/휴면) 배치
     * 항목ID : COMUM00056
     * 항목명 : 비밀번호 만료자(상담사)) 에 대한 상태값 변경 이력 저장 Step
     * 내용   : 비밀번호 만료 대상자에 대하여 상태값 변경 이력을 저장
     * */
    @Bean
    public Step accountExpireUserLogStep() {
        return new StepBuilder("accountExpireUserLogStep", jobRepository)
                .<Account, AccountStsChng>chunk(CHUNK_SIZE, transactionManager)
                .reader(new AccountExpireItemReader(entityManagerFactory))
                .processor(new AccountExpireLogItemProcessor())
                .writer(new AccountStsChngItemWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }

    /*
     * 공통 - 계정관리 - 계정 상태 업데이트(퇴사/비밀번호변경/휴면) 배치
     * 항목ID : COMUM00057
     * 항목명 : 비밀번호 만료자(상담사) 상태값(임시비밀번호 부여) 변경 Step
     * 내용   : 비밀번호 만료 대상자에 대하여 상태값 변경 처리(T:임시비밀번호 상태)
     * */
    @Bean
    public Step accountExpireFinalStep() {
        return new StepBuilder("accountExpireFinalStep", jobRepository)
                .<Account, Account>chunk(CHUNK_SIZE, transactionManager)
                .reader(new AccountExpireReItemReader(entityManagerFactory))
                .processor(new AccountExpireTempItemProcessor(accountRepository))
                .writer(new AccountItemWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }

    /*
     * 공통 - 계정관리 - 계정 상태 업데이트(퇴사/비밀번호변경/휴면) 배치
     * 항목ID : COMUM00058
     * 항목명 : 장기미접속 계정 추출 Step
     * 내용   : 장기미접속 대상자 추출(기준 : 접속한지 2개월이 지난 사용자)
     * */
    @Bean
    @JobScope
    public Step updateDormantUserStep() {
        return new StepBuilder("updateDormantUserStep", jobRepository)
                .<Account, Account>chunk(CHUNK_SIZE, transactionManager)
                .reader(new DormantUserItemReader(entityManagerFactory))
                .processor(new DormantUserItemProcessor())
                .writer(new AccountItemWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }

    /*
     * 공통 - 계정관리 - 계정 상태 업데이트(퇴사/비밀번호변경/휴면) 배치
     * 항목ID : COMUM00059
     * 항목명 : 장기미접속 계정 상태 변경 이력 저장 Step
     * 내용   : 장기미접속 대상자 상태 변경 이력 저장
     * */
    @Bean
    public Step accountDormantUserLogStep() {
        return new StepBuilder("accountDormantUserLogStep", jobRepository)
                .<Account, AccountStsChng>chunk(CHUNK_SIZE, transactionManager)
                .reader(new AccountDormantItemReader(entityManagerFactory))
                .processor(new AccountDormantLogItemProcessor())
                .writer(new AccountStsChngItemWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }

    /*
     * 공통 - 계정관리 - 계정 상태 업데이트(퇴사/비밀번호변경/휴면) 배치
     * 항목ID : COMUM00060
     * 항목명 : 장기미접속 계정 상태값(잠김) 변경 Step
     * 내용   : 장기미접속 대상자 상태 변경(L:잠김 상태)
     * */
    @Bean
    public Step accountDormantFinalStep() {
        return new StepBuilder("accountDormantFinalStep", jobRepository)
                .<Account, Account>chunk(CHUNK_SIZE, transactionManager)
                .reader(new AccountDormantReItemReader(entityManagerFactory))
                .processor(new AccountDormantTempItemProcessor(accountRepository))
                .writer(new AccountItemWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }
}

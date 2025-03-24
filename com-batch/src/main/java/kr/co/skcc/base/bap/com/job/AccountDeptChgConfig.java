package kr.co.skcc.base.bap.com.job;

import jakarta.persistence.EntityManagerFactory;
import kr.co.skcc.base.bap.com.job.listener.CommonJobListener;
import kr.co.skcc.base.bap.com.job.listener.CommonStepListener;
import kr.co.skcc.base.bap.com.job.processor.*;
import kr.co.skcc.base.bap.com.job.reader.*;
import kr.co.skcc.base.bap.com.job.writer.*;
import kr.co.skcc.base.bap.com.repository.*;
import kr.co.skcc.base.bap.com.job.processor.*;
import kr.co.skcc.base.bap.com.job.reader.*;
import kr.co.skcc.base.bap.com.job.writer.*;
import kr.co.skcc.base.bap.com.repository.*;
import kr.co.skcc.base.com.account.domain.account.Account;
import kr.co.skcc.base.com.account.domain.auth.UserRole;
import kr.co.skcc.base.com.account.domain.hist.AccountStsChng;
import kr.co.skcc.base.com.account.domain.hist.UserAuthReqHis;
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
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;


@Configuration
@Slf4j
public class AccountDeptChgConfig {

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
    UserRoleDeptRepository userRoleDeptRepository;

    @Autowired
    UserRoleDeptMappingRepository userRoleDeptMappingRepository;

    @Autowired
    ShortcutMenuRepository shortcutMenuRepository;

    @Autowired
    BookmarkMenuRepository bookmarkMenuRepository;

    @Autowired
    AccountStsChngRepository accountStsChngRepository;

    @Autowired
    DeptRepository deptRepository;

    @Autowired
    UserMenuHistRepository userMenuHistRepository;

    @Autowired
    UserScrenBttnHistRepository userScrenBttnHistRepository;
    private final static int CHUNK_SIZE = 5;

    /*
     * 공통 - 계정관리 - 인사정보 기준 부서/직책 변경 데이터 업데이트 배치
     * 항목ID : COMUM00039
     * 항목명 : 인사정보 기준 변경 데이터 업데이트 배치 Job
     * 내용   : 사원정보가 변경된 사용자에 대한 변경 처리 배치
     * */
    @Bean
    public Job accountDeptChgJob() {
        return new JobBuilder("accountDeptChgJob", jobRepository)
                .preventRestart()
                .start(searchDeptChgStep())
                .next(userRoleDeptChgSearchAddRoleStep())
                .next(accountDeptChgLogStep())
                .next(accountRoleDeleteLogStep())
                .next(accountAddAuthDeleteLogStep())
                .next(userRoleDeptChgMakeRoleStep())
                .next(userRoleDeptChgLogStep())
                .next(userRoleDeptDelMenuStep())
                .next(accountDeptChgFinalStep())
                .next(userStsChgSearchStep())
                .listener(commonJobListener)
                .build();
    }

    /*
     * 공통 - 계정관리 - 인사정보 기준 부서/직책 변경 데이터 업데이트 배치
     * 항목ID : COMUM00040
     * 항목명 : 부서/직책/직능이 변경된 사용자 추출 Step
     * 내용   : 권한(역할)과 관련된 부서/직책/직능이 변경된 사용자 추출
     * */
    @Bean
    public Step searchDeptChgStep() {
        return new StepBuilder("searchDeptChgStep", jobRepository)
                .<UserBasic, Account>chunk(CHUNK_SIZE, transactionManager)
                .reader(new SearchDeptChgItemReader(entityManagerFactory))
                .processor(new SearchDeptChgItemProcessor(accountRepository))
                .writer(new AccountItemWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }

    /*
     * 공통 - 계정관리 - 인사정보 기준 부서/직책 변경 데이터 업데이트 배치
     * 항목ID : COMUM00041
     * 항목명 : 지사/영업소 시준 직능만 변경된 계정 추출 Step
     * 내용   : 지사/영업소 구성원은 직능도 권한(역할)에 영향이 있어 별도로 추출
     * */
    @Bean
    public Step userRoleDeptChgSearchAddRoleStep() {
        return new StepBuilder("userRoleDeptChgSearchAddRoleStep", jobRepository)
                .<Account, Account>chunk(CHUNK_SIZE, transactionManager)
                .reader(new AccountAddRoleDeptChgItemReader(entityManagerFactory))
                .processor(new SearchDeptChgAddItemProcessor(accountRepository))
                .writer(new AccountItemWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }

    /*
     * 공통 - 계정관리 - 인사정보 기준 부서/직책 변경 데이터 업데이트 배치
     * 항목ID : COMUM00042
     * 항목명 : 부서/직책/직능이 변경된 사용자에 대한 이력 저장 Step
     * 내용   : COMUM00041, COMUM00042 에서 추출된 사용자에 대한 변경이력 저장
     * */
    @Bean
    public Step accountDeptChgLogStep() {
        return new StepBuilder("accountDeptChgLogStep", jobRepository)
                .<Account, List<AccountStsChng>>chunk(CHUNK_SIZE, transactionManager)
                .reader(new AccountStsDeptChgItemReader(entityManagerFactory))
                .processor(new AccountStsDeptChgLogItemProcessor())
                .writer(new AccountStsChngListItemWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }

    /*
     * 공통 - 계정관리 - 인사정보 기준 부서/직책 변경 데이터 업데이트 배치
     * 항목ID : COMUM00043
     * 항목명 : 추출된 계정에 대하여 기본 역할 삭제 및 이력 저장 Step
     * 내용   : COMUM00041, COMUM00042 에서 추출된 사용자에 대해 기존에 부여된 기본역할 삭제 및 이력 저장
     * */
    @Bean
    public Step accountRoleDeleteLogStep() {
        return new StepBuilder("accountRoleDeleteLogStep", jobRepository)
                .<Account, List<UserRoleHist>>chunk(CHUNK_SIZE, transactionManager)
                .reader(new AccountStsDeptChgItemReader(entityManagerFactory))
                .processor(new AccountRoleDeleteLogItemProcessor(userRoleRepository, deptRepository, accountRepository))
                .writer(new UserRoleHistItemListWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }

    /*
     * 공통 - 계정관리 - 인사정보 기준 부서/직책 변경 데이터 업데이트 배치
     * 항목ID : COMUM00044
     * 항목명 : 추출된 계정에 대하여 추가권한 삭제 및 이력 저장 Step
     * 내용   : COMUM00041, COMUM00042 에서 추출된 사용자에 대해 기존에 부여된 추가권한 삭제 이력 저장
     * */
    @Bean
    public Step accountAddAuthDeleteLogStep() {
        return new StepBuilder("accountAddAuthDeleteLogStep", jobRepository)
                .<Account, List<UserAuthReqHis>>chunk(CHUNK_SIZE, transactionManager)
                .reader(new AccountStsDeptChgItemReader(entityManagerFactory))
                .processor(new AccountAddAuthDeleteLogItemProcessor(userRoleRepository,
                                                                    userMenuRepository,
                                                                    userScrenBttnRepository,
                                                                    userMenuHistRepository,
                                                                    userScrenBttnHistRepository))
                .writer(new UserAuthReqHisItemListWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }

    /*
     * 공통 - 계정관리 - 인사정보 기준 부서/직책 변경 데이터 업데이트 배치
     * 항목ID : COMUM00045
     * 항목명 : 추출된 계정에 대해 변경된 기본 역할 부여 Step
     * 내용   : COMUM00041, COMUM00042 에서 추출된 사용자에 대해 변경된 부서/직책/직능에 맞는 기본 역할 부여
     * */
    @Bean
    public Step userRoleDeptChgMakeRoleStep() {
        return new StepBuilder("userRoleDeptChgMakeRoleStep", jobRepository)
                .<Account, List<UserRole>>chunk(CHUNK_SIZE, transactionManager)
                .reader(new AccountStsDeptChgItemReader(entityManagerFactory))
                .processor(new UserRoleSetItemProcessor(userRoleDeptRepository
                                                        , userRoleRepository
                                                        , userMenuRepository
                                                        , userScrenBttnRepository
                                                        , userRoleDeptMappingRepository))
                .writer(new UserRoleItemListWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }

    /*
     * 공통 - 계정관리 - 인사정보 기준 부서/직책 변경 데이터 업데이트 배치
     * 항목ID : COMUM00046
     * 항목명 : 추출된 계정에 대해 변경된 역할 이력 저장 Step
     * 내용   : COMUM00045 에서 변경된 역할 이력을 저장
     * */
    @Bean
    public Step userRoleDeptChgLogStep() {
        return new StepBuilder("userRoleDeptChgLogStep", jobRepository)
                .<Account, List<UserRoleHist>>chunk(CHUNK_SIZE, transactionManager)
                .reader(new AccountStsDeptChgItemReader(entityManagerFactory))
                .processor(new UserRoleDeptChgLogItemProcessor(userRoleRepository, deptRepository))
                .writer(new UserRoleHistItemListWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }

    /*
     * 공통 - 계정관리 - 인사정보 기준 부서/직책 변경 데이터 업데이트 배치
     * 항목ID : COMUM00047
     * 항목명 : 추출된 계정에 대해 권한이 없는 메뉴에 대한 즐겨찾기/바로가기 메뉴 삭제 Step
     * 내용   : COMUM00045 에서 변경된 역할에 없는 메뉴들을 즐겨찾기/바로가기 메뉴에서 삭제
     * */
    @Bean
    public Step userRoleDeptDelMenuStep() {
        return new StepBuilder("userRoleDeptDelMenuStep", jobRepository)
                .<Account, Account>chunk(CHUNK_SIZE, transactionManager)
                .reader(new AccountStsDeptChgItemReader(entityManagerFactory))
                .processor(new UserRoleDeptDelMenuItemProcessor(shortcutMenuRepository, bookmarkMenuRepository))
                .writer(new NoOpItemWriter())
                // .listener(commonStepListener)
                .build();
    }

    /*
     * 공통 - 계정관리 - 인사정보 기준 부서/직책 변경 데이터 업데이트 배치
     * 항목ID : COMUM00048
     * 항목명 : 추출된 계정 상태 사용가능하도록 변경 처리 Step
     * 내용   : COMUM00041, COMUM00042 에서 추출된 사용자에 대해 사용 가능하도록 상태값 변경
     * */
    @Bean
    public Step accountDeptChgFinalStep() {
        return new StepBuilder("accountDeptChgFinalStep", jobRepository)
                .<Account, Account>chunk(CHUNK_SIZE, transactionManager)
                .reader(new AccountStsDeptChgReItemReader(entityManagerFactory))
                .processor(new AccountStsTempItemProcessor(accountRepository))
                .writer(new AccountItemWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }

    /*
     * 공통 - 계정관리 - 인사정보 기준 부서/직책 변경 데이터 업데이트 배치
     * 항목ID : COMUM00048
     * 항목명 : 권한과 관련되지 않는 정보(휴대폰 번호, 이름 등) 변경에 대한 처리 및 이력 저장 Step
     * 내용   : 권한과 관계 없는 기본 정보에 대한 변경에 대한 내용 저장 및 이력 저장
     * */
    @Bean
    public Step userStsChgSearchStep() {
        return new StepBuilder("userRoleDeptChgSearchAddRoleStep", jobRepository)
                .<Account, Account>chunk(CHUNK_SIZE, transactionManager)
                .reader(new UserStsChgSearchItemReader(entityManagerFactory))
                .processor(new UserStsChgSearchItemProcessor(accountRepository, accountStsChngRepository))
                .writer(new AccountItemWriter(entityManagerFactory))
                .listener(commonStepListener)
                .build();
    }
}
package kr.co.skcc.oss.bap.com.job.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.skcc.oss.bap.com.repository.AccountRepository;
import kr.co.skcc.oss.bap.com.repository.DeptRepository;
import kr.co.skcc.oss.bap.com.repository.UserRoleHistRepository;
import kr.co.skcc.oss.bap.com.repository.UserRoleRepository;
import kr.co.skcc.oss.com.account.api.dto.domainDto.AccountDto;
import kr.co.skcc.oss.com.account.api.type.AuthType;
import kr.co.skcc.oss.com.account.domain.auth.Agent;
import kr.co.skcc.oss.com.account.domain.auth.UserRole;
import kr.co.skcc.oss.com.account.domain.auth.pk.UserRolePK;
import kr.co.skcc.oss.com.account.domain.hist.UserRoleHist;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


@Slf4j
public class AgentEndItemProcessor implements ItemProcessor<Object, Agent> {
    UserRoleRepository userRoleRepository;

    UserRoleHistRepository userRoleHistRepository;

    AccountRepository accountRepository;

    DeptRepository deptRepository;
    public AgentEndItemProcessor(UserRoleRepository userRoleRepository,
                                 UserRoleHistRepository userRoleHistRepository,
                                 AccountRepository accountRepository,
                                 DeptRepository deptRepository){
        this.userRoleRepository = userRoleRepository;
        this.userRoleHistRepository = userRoleHistRepository;
        this.accountRepository = accountRepository;
        this.deptRepository = deptRepository;
    }

    @Override
    public Agent process(Object items) {

        ObjectMapper mapper = new ObjectMapper();
        Agent agent = mapper.convertValue(items, Agent.class);

        UserRolePK userRolePK = new UserRolePK(AuthType.AUTH_CONFIRM.getCode(), agent.getAgentId());
        Optional<UserRole> oUserRole = userRoleRepository.findById(userRolePK);
        AccountDto account = accountRepository.findUserDeptInfo(agent.getAgentId());

        // 혹시 팀장급 사용자일 경우 권한 계속 유지
        if(oUserRole.isPresent()
            && !(Integer.parseInt(account.getReofoCd()) >= 112 && Integer.parseInt(account.getReofoCd()) <= 210)){
            // 권한 즉시 제거
            userRoleRepository.deleteById(userRolePK);

            UserRoleHist userRoleHist = new UserRoleHist();
            userRoleHist.setUserid(agent.getAgentId());
            userRoleHist.setUserRoleId(AuthType.AUTH_CONFIRM.getCode());
            userRoleHist.setChngDtm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            userRoleHist.setAthrtyReqstSeq(null);
            userRoleHist.setCrudClCd("D");
            userRoleHist.setDeptcd(account.getDeptcd());
            userRoleHist.setDeptNm(account.getDeptNm());
            userRoleHist.setLastChngrId("BATCH");
            userRoleHist.setLastChngDtmd(LocalDateTime.now());
            userRoleHist.setChngResonCntnt("대무 종료로 인한 권한 제거");

            userRoleHistRepository.save(userRoleHist);
        }

        agent.setLastChngrId("BATCH");
        agent.setLastChngDtmd(LocalDateTime.now());
        agent.setAgentEndDt(LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        agent.setEndYn("Y");

        return agent;

    }
}
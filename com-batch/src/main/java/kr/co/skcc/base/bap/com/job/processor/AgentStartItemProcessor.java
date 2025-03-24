package kr.co.skcc.base.bap.com.job.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.skcc.base.bap.com.repository.AccountRepository;
import kr.co.skcc.base.bap.com.repository.DeptRepository;
import kr.co.skcc.base.bap.com.repository.UserRoleHistRepository;
import kr.co.skcc.base.bap.com.repository.UserRoleRepository;
import kr.co.skcc.base.com.account.api.dto.domainDto.AccountDto;
import kr.co.skcc.base.com.account.api.type.AuthType;
import kr.co.skcc.base.com.account.domain.auth.Agent;
import kr.co.skcc.base.com.account.domain.auth.UserRole;
import kr.co.skcc.base.com.account.domain.auth.pk.UserRolePK;
import kr.co.skcc.base.com.account.domain.hist.UserRoleHist;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


@Slf4j
public class AgentStartItemProcessor implements ItemProcessor<Object, Agent> {
    UserRoleRepository userRoleRepository;

    UserRoleHistRepository userRoleHistRepository;

    AccountRepository accountRepository;

    DeptRepository deptRepository;
    public AgentStartItemProcessor(UserRoleRepository userRoleRepository,
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

        if (!oUserRole.isPresent()) {
            // 권한 추가
            UserRole userRole = new UserRole();
            userRole.setUserRoleId(AuthType.AUTH_CONFIRM.getCode());
            userRole.setLastChngrId("BATCH");
            userRole.setLastChngDtmd(LocalDateTime.now());
            userRole.setUserid(agent.getAgentId());
            userRoleRepository.save(userRole);

            AccountDto account = accountRepository.findUserDeptInfo(agent.getAgentId());

            UserRoleHist userRoleHist = new UserRoleHist();
            userRoleHist.setUserid(agent.getAgentId());
            userRoleHist.setUserRoleId(AuthType.AUTH_CONFIRM.getCode());
            userRoleHist.setChngDtm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            userRoleHist.setAthrtyReqstSeq(null);
            userRoleHist.setCrudClCd("C");
            userRoleHist.setDeptcd(account.getDeptcd());
            userRoleHist.setDeptNm(account.getDeptNm());
            userRoleHist.setLastChngrId("BATCH");
            userRoleHist.setLastChngDtmd(LocalDateTime.now());
            userRoleHist.setChngResonCntnt("대무 등록으로 인한 권한 부여");

            userRoleHistRepository.save(userRoleHist);
        }

        agent.setLastChngrId("BATCH");
        agent.setLastChngDtmd(LocalDateTime.now());

        return agent;
    }
}
package com.skcc.ra.bap.job.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skcc.ra.bap.repository.AccountRepository;
import com.skcc.ra.bap.repository.DeptRepository;
import com.skcc.ra.bap.repository.UserRoleHistRepository;
import com.skcc.ra.bap.repository.UserRoleRepository;
import com.skcc.ra.account.api.dto.domainDto.AccountDto;
import com.skcc.ra.account.api.type.AuthType;
import com.skcc.ra.account.domain.auth.Agent;
import com.skcc.ra.account.domain.auth.UserRole;
import com.skcc.ra.account.domain.auth.pk.UserRolePK;
import com.skcc.ra.account.domain.hist.UserRoleHist;
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
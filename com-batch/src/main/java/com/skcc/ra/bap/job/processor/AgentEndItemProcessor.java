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
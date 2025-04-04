package kr.co.skcc.base.bap.com.job.processor;

import kr.co.skcc.base.bap.com.repository.AccountRepository;
import kr.co.skcc.base.bap.com.repository.UserRoleRepository;
import kr.co.skcc.base.com.account.api.dto.domainDto.AccountDto;
import kr.co.skcc.base.com.account.domain.auth.UserRole;
import kr.co.skcc.base.com.account.domain.hist.UserRoleHist;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class AccountRetireUserRoleLogItemProcessor implements ItemProcessor<Object, List<UserRoleHist>> {

    UserRoleRepository userRoleRepository;

    AccountRepository accountRepository;

    public AccountRetireUserRoleLogItemProcessor(UserRoleRepository userRoleRepository, AccountRepository accountRepository) {
        this.userRoleRepository = userRoleRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public List<UserRoleHist> process(Object items) {

        Object[] objects = (Object[]) items;
        Iterator<Object> iterator = Arrays.stream(objects).iterator();
        List<String> list = new ArrayList<>();
        List<UserRoleHist> userRoleHistList = new ArrayList<>();

        while(iterator.hasNext()){
            String value = String.valueOf(iterator.next());
            list.add(value);
        }

        List<UserRole> userRoleList = userRoleRepository.findByUserid(list.get(0));
        AccountDto accountDto = accountRepository.findUserDeptInfo(list.get(0));

        for(int i =0; i< userRoleList.size(); i++){

            UserRoleHist userRoleHist = new UserRoleHist();
            userRoleHist.setUserid(userRoleList.get(i).getUserid());
            userRoleHist.setUserRoleId(userRoleList.get(i).getUserRoleId());
            userRoleHist.setChngDtm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            userRoleHist.setAthrtyReqstSeq(userRoleList.get(i).getAthrtyReqstSeq());
            userRoleHist.setCrudClCd("D");
            userRoleHist.setDeptcd(accountDto.getDeptcd());
            userRoleHist.setDeptNm(accountDto.getDeptNm());
            userRoleHist.setLastChngrId("BATCH");
            userRoleHist.setChngResonCntnt("퇴사로 인한 권한 삭제");
            userRoleHist.setLastChngDtmd(LocalDateTime.now());
            userRoleHistList.add(userRoleHist);


        }

        return userRoleHistList;
    }
}
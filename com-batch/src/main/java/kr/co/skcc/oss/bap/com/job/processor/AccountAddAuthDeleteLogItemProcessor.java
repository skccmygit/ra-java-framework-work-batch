package kr.co.skcc.oss.bap.com.job.processor;

import kr.co.skcc.oss.bap.com.repository.*;
import kr.co.skcc.oss.com.account.domain.auth.UserMenu;
import kr.co.skcc.oss.com.account.domain.auth.UserScrenBttn;
import kr.co.skcc.oss.com.account.domain.hist.UserAuthReqHis;
import kr.co.skcc.oss.com.account.domain.hist.UserMenuHist;
import kr.co.skcc.oss.com.account.domain.hist.UserScrenBttnHist;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class AccountAddAuthDeleteLogItemProcessor implements ItemProcessor<Object, List<UserAuthReqHis>> {

    UserRoleRepository userRoleRepository;
    UserMenuRepository userMenuRepository;
    UserScrenBttnRepository userScrenBttnRepository;
    UserMenuHistRepository userMenuHistRepository;
    UserScrenBttnHistRepository userScrenBttnHistRepository;

    public AccountAddAuthDeleteLogItemProcessor(UserRoleRepository userRoleRepository,
                                                UserMenuRepository userMenuRepository,
                                                UserScrenBttnRepository userScrenBttnRepository,
                                                UserMenuHistRepository userMenuHistRepository,
                                                UserScrenBttnHistRepository userScrenBttnHistRepository) {
        this.userRoleRepository = userRoleRepository;
        this.userMenuRepository = userMenuRepository;
        this.userScrenBttnRepository = userScrenBttnRepository;
        this.userMenuHistRepository = userMenuHistRepository;
        this.userScrenBttnHistRepository = userScrenBttnHistRepository;
    }

    @Override
    public List<UserAuthReqHis> process(Object items) {

        Object[] objects = (Object[]) items;
        Iterator<Object> iterator = Arrays.stream(objects).iterator();
        List<String> list = new ArrayList<>();

        while(iterator.hasNext()){
            String value = String.valueOf(iterator.next());
            list.add(value);
        }

        String chngResonCntnt = "부서변경으로 인한 추가권한 삭제";
        List<UserMenu> userMenuList = userMenuRepository.findByUserid(list.get(0));
        for(UserMenu userMenu : userMenuList){
            UserMenuHist userMenuHist = new UserMenuHist();

            userMenuHist.setUserid(userMenu.getUserid());
            userMenuHist.setMenuId(userMenu.getMenuId());
            userMenuHist.setChngDtm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            userMenuHist.setLastChngDtmd(LocalDateTime.now());
            userMenuHist.setLastChngrId("BATCH");
            userMenuHist.setCrudClCd("D");
            userMenuHist.setChngUserIpaddr("");
            userMenuHist.setChngResonCntnt(chngResonCntnt);

            userMenuHistRepository.save(userMenuHist);
        }

        //추가 화면/버튼 이력생성
        List<UserScrenBttn> userScrenBttnList = userScrenBttnRepository.findByUserid(list.get(0));
        for(UserScrenBttn userScrenBttn : userScrenBttnList){
            UserScrenBttnHist userScrenBttnHist = new UserScrenBttnHist();

            userScrenBttnHist.setUserid(userScrenBttn.getUserid());
            userScrenBttnHist.setScrenId(userScrenBttn.getScrenId());
            userScrenBttnHist.setBttnId(userScrenBttn.getBttnId());
            userScrenBttnHist.setChngDtm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            userScrenBttnHist.setLastChngDtmd(LocalDateTime.now());
            userScrenBttnHist.setLastChngrId("BATCH");
            userScrenBttnHist.setCrudClCd("D");
            userScrenBttnHist.setChngUserIpaddr("");
            userScrenBttnHist.setChngResonCntnt(chngResonCntnt);

            userScrenBttnHistRepository.save(userScrenBttnHist);
        }

        return null;
    }
}
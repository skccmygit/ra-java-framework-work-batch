package com.skcc.ra.bap.job.processor;

import com.skcc.ra.bap.repository.*;
import com.skcc.ra.bap.repository.*;
import com.skcc.ra.account.domain.account.Account;
import com.skcc.ra.account.domain.auth.UserMenu;
import com.skcc.ra.account.domain.auth.UserScrenBttn;
import com.skcc.ra.account.domain.hist.UserMenuHist;
import com.skcc.ra.account.domain.hist.UserScrenBttnHist;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class AccountRetireTempItemProcessor implements ItemProcessor<Object, Account> {

    AccountRepository accountRepository;
    UserRoleRepository userRoleRepository;
    UserMenuRepository userMenuRepository;
    UserScrenBttnRepository userScrenBttnRepository;
    ShortcutMenuRepository shortcutMenuRepository;
    BookmarkMenuRepository bookmarkMenuRepository;
    MyViewRepository myViewRepository;
    MyViewDtlRepository myViewDtlRepository;

    UserMenuHistRepository userMenuHistRepository;

    UserScrenBttnHistRepository userScrenBttnHistRepository;


    public AccountRetireTempItemProcessor(AccountRepository accountRepository,
                                          UserRoleRepository userRoleRepository,
                                          UserMenuRepository userMenuRepository,
                                          UserScrenBttnRepository userScrenBttnRepository,
                                          ShortcutMenuRepository shortcutMenuRepository,
                                          BookmarkMenuRepository bookmarkMenuRepository,
                                          MyViewRepository myViewRepository,
                                          MyViewDtlRepository myViewDtlRepository,
                                          UserMenuHistRepository userMenuHistRepository,
                                          UserScrenBttnHistRepository userScrenBttnHistRepository) {
        this.accountRepository = accountRepository;
        this.userRoleRepository = userRoleRepository;
        this.userMenuRepository = userMenuRepository;
        this.userScrenBttnRepository = userScrenBttnRepository;
        this.shortcutMenuRepository = shortcutMenuRepository;
        this.bookmarkMenuRepository = bookmarkMenuRepository;
        this.myViewRepository = myViewRepository;
        this.myViewDtlRepository = myViewDtlRepository;
        this.userMenuHistRepository = userMenuHistRepository;
        this.userScrenBttnHistRepository = userScrenBttnHistRepository;
    }
    @Override
    public Account process(Object items) {

        Object[] objects = (Object[]) items;
        Iterator<Object> iterator = Arrays.stream(objects).iterator();
        List<String> list = new ArrayList<>();

        while(iterator.hasNext()){
            String value = String.valueOf(iterator.next());
            list.add(value);
        }
        if(accountRepository.existsById(list.get(0))) {
            Account account = accountRepository.getById(list.get(0));

            account.setUseridStsCd("D");
            account.setLastChngDtmd(LocalDateTime.now());

            userRoleRepository.deleteByUserid(account.getUserid());
            String chngResonCntnt = "퇴사로 인한 추가권한 삭제";

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

            //추가 메뉴 삭제
            userMenuRepository.deleteByUserid(account.getUserid());
            //추가 화면/버튼 삭제
            userScrenBttnRepository.deleteByUserid(account.getUserid());
            //바로가기 삭제
            shortcutMenuRepository.deleteByUserid(account.getUserid());
            //즐겨찾기 삭제
            bookmarkMenuRepository.deleteByUserid(account.getUserid());
            //마이뷰 상세 삭제
            myViewDtlRepository.deleteMyViewDtl(account.getUserid());
            //마이뷰 삭제
            myViewRepository.deleteByUserid(account.getUserid());


            return account;
        }
        return null;
    }
}
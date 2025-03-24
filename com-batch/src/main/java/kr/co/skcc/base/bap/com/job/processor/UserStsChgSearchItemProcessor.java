package kr.co.skcc.base.bap.com.job.processor;

import kr.co.skcc.base.bap.com.repository.AccountRepository;
import kr.co.skcc.base.bap.com.repository.AccountStsChngRepository;
import kr.co.skcc.base.com.account.domain.account.Account;
import kr.co.skcc.base.com.account.domain.hist.AccountStsChng;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class UserStsChgSearchItemProcessor implements ItemProcessor<Object, Account> {

    AccountRepository accountRepository;
    AccountStsChngRepository accountStsChngRepository;
    public UserStsChgSearchItemProcessor(AccountRepository accountRepository, AccountStsChngRepository accountStsChngRepository) {
        this.accountRepository = accountRepository;
        this.accountStsChngRepository = accountStsChngRepository;
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

        Account account = accountRepository.getById(list.get(0));
        List<AccountStsChng> accountStsChngList = new ArrayList<>();

        if(!list.get(1).equals(account.getUserNm())){
            account.setUserNm(list.get(1));

            AccountStsChng accountStsChng = new AccountStsChng();

            accountStsChng.setUserid(list.get(0));
            accountStsChng.setChngDtm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            accountStsChng.setLastChngDtmd(LocalDateTime.now());
            accountStsChng.setLastChngrId("BATCH");
            accountStsChng.setChngColEngshNm("USER_NM");
            accountStsChng.setChngColVal(list.get(1));

            accountStsChngList.add(accountStsChng);
        }

        if(!list.get(2).equals(account.getUserContPhno())){
            account.setUserContPhno(list.get(2));

            AccountStsChng accountStsChng = new AccountStsChng();

            accountStsChng.setUserid(list.get(0));
            accountStsChng.setChngDtm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            accountStsChng.setLastChngDtmd(LocalDateTime.now());
            accountStsChng.setLastChngrId("BATCH");
            accountStsChng.setChngColEngshNm("USER_CONN_PHNO");
            accountStsChng.setChngColVal(list.get(2));

            accountStsChngList.add(accountStsChng);
        }

        if(!list.get(3).equals(account.getVctnCd())){
            account.setVctnCd(list.get(3));

            AccountStsChng accountStsChng = new AccountStsChng();

            accountStsChng.setUserid(list.get(0));
            accountStsChng.setChngDtm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            accountStsChng.setLastChngDtmd(LocalDateTime.now());
            accountStsChng.setLastChngrId("BATCH");
            accountStsChng.setChngColEngshNm("VCTN_CD");
            accountStsChng.setChngColVal(list.get(3));

            accountStsChngList.add(accountStsChng);
        }

        accountStsChngRepository.saveAll(accountStsChngList);

        return account;
    }
}
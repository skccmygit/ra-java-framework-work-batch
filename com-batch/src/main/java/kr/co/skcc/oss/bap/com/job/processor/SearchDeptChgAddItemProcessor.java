package kr.co.skcc.oss.bap.com.job.processor;

import kr.co.skcc.oss.bap.com.repository.AccountRepository;
import kr.co.skcc.oss.com.account.domain.account.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class SearchDeptChgAddItemProcessor implements ItemProcessor<Object, Account> {

    AccountRepository accountRepository;

    public SearchDeptChgAddItemProcessor(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
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

        account.setDeptcd(list.get(1));
        account.setReofoCd(list.get(2));
        account.setVctnCd(list.get(3));
        account.setUseridStsCd("7");
        account.setLastChngDtmd(LocalDateTime.now());
        account.setLastChngrId("BATCH");

        return account;
    }
}
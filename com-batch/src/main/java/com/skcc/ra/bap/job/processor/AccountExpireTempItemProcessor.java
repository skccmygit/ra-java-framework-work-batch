package com.skcc.ra.bap.job.processor;

import com.skcc.ra.bap.repository.AccountRepository;
import com.skcc.ra.account.domain.account.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class AccountExpireTempItemProcessor implements ItemProcessor<Object, Account> {

    AccountRepository accountRepository;

    public AccountExpireTempItemProcessor(AccountRepository accountRepository) {
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
        if(accountRepository.existsById(list.get(0))) {
            Account account = accountRepository.getById(list.get(0));

            account.setUseridStsCd("T");
            account.setLastChngDtmd(LocalDateTime.now());

            return account;
        }
        return null;
    }
}
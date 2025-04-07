package com.skcc.ra.bap.job.processor;

import com.skcc.ra.account.domain.account.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class DormantUserItemProcessor implements ItemProcessor<Account, Account> {

    @Override
    public Account process(Account account) {
        log.info("userName ={}",account.getUserid());
        account.setUseridStsCd("C");
        return account;
    }
}
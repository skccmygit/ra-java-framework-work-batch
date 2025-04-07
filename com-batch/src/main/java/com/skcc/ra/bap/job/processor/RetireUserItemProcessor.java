package com.skcc.ra.bap.job.processor;

import com.skcc.ra.account.domain.account.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class RetireUserItemProcessor implements ItemProcessor<Account, Account> {

    @Override
    public Account process(Account account) {
        log.info("userName ={}",account.getUserid());
        String maskedName = account.getUserNm().substring(0,1) + "**";

        account.setUseridStsCd("A");
        account.setConnPsswd("");
        account.setUserNm(maskedName);
        account.setUserContPhno("");
        account.setUserEmailaddr("");

        return account;
    }
}
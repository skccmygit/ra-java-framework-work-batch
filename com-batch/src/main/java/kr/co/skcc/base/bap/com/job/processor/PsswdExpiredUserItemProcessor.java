package kr.co.skcc.base.bap.com.job.processor;

import kr.co.skcc.base.com.account.domain.account.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class PsswdExpiredUserItemProcessor implements ItemProcessor<Account, Account> {

    @Override
    public Account process(Account account) {
        log.info("userName ={}",account.getUserNm());
        account.setUseridStsCd("B");
        return account;
    }
}
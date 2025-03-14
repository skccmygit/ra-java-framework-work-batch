package kr.co.skcc.oss.bap.com.job.writer;

import jakarta.persistence.EntityManagerFactory;
import kr.co.skcc.oss.com.account.domain.hist.AccountStsChng;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountStsChngListItemWriter extends JpaItemListWriter<AccountStsChng> {

    public AccountStsChngListItemWriter(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }
}
package com.skcc.ra.bap.job.writer;

import jakarta.persistence.EntityManagerFactory;
import com.skcc.ra.account.domain.hist.AccountStsChng;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountStsChngListItemWriter extends JpaItemListWriter<AccountStsChng> {

    public AccountStsChngListItemWriter(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }
}
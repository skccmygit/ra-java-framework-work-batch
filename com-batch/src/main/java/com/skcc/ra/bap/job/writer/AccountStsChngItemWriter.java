package com.skcc.ra.bap.job.writer;

import jakarta.persistence.EntityManagerFactory;
import com.skcc.ra.account.domain.hist.AccountStsChng;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.JpaItemWriter;

@Slf4j
public class AccountStsChngItemWriter extends JpaItemWriter<AccountStsChng> {

    public AccountStsChngItemWriter(EntityManagerFactory entityManagerFactory) {
        this.setEntityManagerFactory(entityManagerFactory);
    }
}
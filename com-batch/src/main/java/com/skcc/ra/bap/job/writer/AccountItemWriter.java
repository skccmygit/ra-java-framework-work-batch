package com.skcc.ra.bap.job.writer;

import jakarta.persistence.EntityManagerFactory;
import com.skcc.ra.account.domain.account.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.JpaItemWriter;

@Slf4j
public class AccountItemWriter extends JpaItemWriter<Account> {

    public AccountItemWriter(EntityManagerFactory entityManagerFactory) {
        this.setEntityManagerFactory(entityManagerFactory);
    }
}
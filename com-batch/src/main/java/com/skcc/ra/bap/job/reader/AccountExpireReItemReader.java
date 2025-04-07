package com.skcc.ra.bap.job.reader;

import jakarta.persistence.EntityManagerFactory;
import com.skcc.ra.account.domain.account.Account;
import org.springframework.batch.item.database.JpaPagingItemReader;

import java.util.HashMap;
import java.util.Map;


public class AccountExpireReItemReader extends JpaPagingItemReader<Account> {

    private final static int CHUNK_SIZE = 5;

    @Override
    public int getPage() {
        return 0;
    }

    public AccountExpireReItemReader(EntityManagerFactory entityManagerFactory) {

        String query = "SELECT t.userid, t.deptcd " +
                         "FROM Account t " +
                        "WHERE t.useridStsCd = 'B' ";
        this.setQueryString(query);
        this.setPageSize(CHUNK_SIZE);
        this.setEntityManagerFactory(entityManagerFactory);
        this.setName("AccountRetireItemReader");
        Map<String, Object> map = new HashMap<>();
        this.setParameterValues(map);
    }
}
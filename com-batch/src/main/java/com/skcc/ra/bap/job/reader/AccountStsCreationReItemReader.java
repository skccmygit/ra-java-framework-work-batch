package com.skcc.ra.bap.job.reader;

import jakarta.persistence.EntityManagerFactory;
import com.skcc.ra.account.domain.account.Account;
import org.springframework.batch.item.database.JpaPagingItemReader;

import java.util.HashMap;
import java.util.Map;

public class AccountStsCreationReItemReader extends JpaPagingItemReader<Account> {

    private final static int CHUNK_SIZE = 5;

    @Override
    public int getPage() {
        return 0;
    }

    public AccountStsCreationReItemReader(EntityManagerFactory entityManagerFactory) {

        String query = "SELECT t.userid, t.deptcd, t.reofoCd, t.useridStsCd, t.vctnCd \n" +
                       "  FROM Account t \n" +
                       " WHERE t.useridStsCd IN ('Y', 'X') \n";
        this.setQueryString(query);
        this.setPageSize(CHUNK_SIZE);
        this.setEntityManagerFactory(entityManagerFactory);
        this.setName("AccountStsLogItemReader");
        Map<String, Object> map = new HashMap<>();
        this.setParameterValues(map);
    }
}
package com.skcc.ra.bap.job.reader;

import jakarta.persistence.EntityManagerFactory;
import com.skcc.ra.account.domain.account.Account;
import org.springframework.batch.item.database.JpaPagingItemReader;

import java.util.HashMap;
import java.util.Map;


public class AccountStsDeptChgReItemReader extends JpaPagingItemReader<Account> {

    private final static int CHUNK_SIZE = 5;

    @Override
    public int getPage() {
        return 0;
    }

    public AccountStsDeptChgReItemReader(EntityManagerFactory entityManagerFactory) {

        String query = "SELECT t.userid, t.deptcd, t.reofoCd, t.useridStsCd " +
                         "FROM Account t " +
                        "WHERE t.useridStsCd IN ('1','2','3','4','5','6','7') ";
        this.setQueryString(query);
        this.setPageSize(CHUNK_SIZE);
        this.setEntityManagerFactory(entityManagerFactory);
        this.setName("AccountStsDeptChgItemReader");
        Map<String, Object> map = new HashMap<>();
        this.setParameterValues(map);
    }
}
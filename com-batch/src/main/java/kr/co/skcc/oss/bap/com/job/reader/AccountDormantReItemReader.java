package kr.co.skcc.oss.bap.com.job.reader;

import jakarta.persistence.EntityManagerFactory;
import kr.co.skcc.oss.com.account.domain.account.Account;
import org.springframework.batch.item.database.JpaPagingItemReader;

import java.util.HashMap;
import java.util.Map;


public class AccountDormantReItemReader extends JpaPagingItemReader<Account> {

    private final static int CHUNK_SIZE = 5;

    @Override
    public int getPage() {
        return 0;
    }

    public AccountDormantReItemReader(EntityManagerFactory entityManagerFactory) {

        String query = "SELECT t.userid, t.deptcd " +
                         "FROM Account t " +
                        "WHERE t.useridStsCd = 'C' ";
        this.setQueryString(query);
        this.setPageSize(CHUNK_SIZE);
        this.setEntityManagerFactory(entityManagerFactory);
        this.setName("AccountRetireItemReader");
        Map<String, Object> map = new HashMap<>();
        this.setParameterValues(map);
    }
}
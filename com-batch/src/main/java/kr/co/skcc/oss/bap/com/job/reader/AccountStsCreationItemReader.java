package kr.co.skcc.oss.bap.com.job.reader;

import jakarta.persistence.EntityManagerFactory;
import kr.co.skcc.oss.com.account.domain.account.Account;
import org.springframework.batch.item.database.JpaPagingItemReader;

import java.util.HashMap;
import java.util.Map;


public class AccountStsCreationItemReader extends JpaPagingItemReader<Account> {

    private final static int CHUNK_SIZE = 5;

    public AccountStsCreationItemReader(EntityManagerFactory entityManagerFactory) {

        String query = "SELECT t.userid, t.deptcd, t.reofoCd, t.useridStsCd, t.vctnCd, d.deptNm \n" +
                       "  FROM Account t \n" +
                       "  LEFT OUTER JOIN Dept d ON d.deptcd = t.deptcd \n" +
                       " WHERE t.useridStsCd IN ('X', 'Y') \n";
        this.setQueryString(query);
        this.setPageSize(CHUNK_SIZE);
        this.setEntityManagerFactory(entityManagerFactory);
        this.setName("AccountStsLogItemReader");
        Map<String, Object> map = new HashMap<>();
        this.setParameterValues(map);
    }
}
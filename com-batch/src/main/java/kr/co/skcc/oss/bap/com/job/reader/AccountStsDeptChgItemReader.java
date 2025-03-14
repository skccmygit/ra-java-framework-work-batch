package kr.co.skcc.oss.bap.com.job.reader;

import jakarta.persistence.EntityManagerFactory;
import kr.co.skcc.oss.com.account.domain.account.Account;
import org.springframework.batch.item.database.JpaPagingItemReader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AccountStsDeptChgItemReader extends JpaPagingItemReader<Account> {

    private final static int CHUNK_SIZE = 5;

    public AccountStsDeptChgItemReader(EntityManagerFactory entityManagerFactory) {

        String query = "SELECT t.userid, t.deptcd, t.reofoCd, t.useridStsCd, t.vctnCd, d.deptNm " +
                       "  FROM Account t " +
                       "  LEFT OUTER JOIN Dept d ON d.deptcd = t.deptcd \n" +
                        "WHERE t.useridStsCd IN :useridStsCd ";
        this.setQueryString(query);
        this.setPageSize(CHUNK_SIZE);
        this.setEntityManagerFactory(entityManagerFactory);
        this.setName("AccountStsDeptChgItemReader");
        Map<String, Object> map = new HashMap<>();
        List<String> useridStsCd = Arrays.asList("1","2", "3","4","5","6", "7");
        map.put("useridStsCd",useridStsCd);
        this.setParameterValues(map);
    }
}
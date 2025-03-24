package kr.co.skcc.base.bap.com.job.reader;

import jakarta.persistence.EntityManagerFactory;
import kr.co.skcc.base.com.account.domain.account.Account;
import org.springframework.batch.item.database.JpaPagingItemReader;

import java.util.HashMap;
import java.util.Map;


public class UserStsChgSearchItemReader extends JpaPagingItemReader<Account> {

    private final static int CHUNK_SIZE = 5;

    @Override
    public int getPage() {
        return 0;
    }

    public UserStsChgSearchItemReader(EntityManagerFactory entityManagerFactory) {

        String query = "SELECT t.empno, t.empKrnNm, t.mphno, t.vctnCd \n" +
                "  FROM UserBasic t \n" +
                " INNER JOIN Account e ON e.userid = t.empno \n" +
                " WHERE t.empKrnNm <> e.userNm  \n" +
                "    OR t.mphno <> e.userContPhno  \n" +
                "    OR t.vctnCd <> e.vctnCd  \n";
        this.setQueryString(query);
        this.setPageSize(CHUNK_SIZE);
        this.setEntityManagerFactory(entityManagerFactory);
        this.setName("UserStsChgSearchItemReader");
        Map<String, Object> map = new HashMap<>();
        this.setParameterValues(map);
    }
}
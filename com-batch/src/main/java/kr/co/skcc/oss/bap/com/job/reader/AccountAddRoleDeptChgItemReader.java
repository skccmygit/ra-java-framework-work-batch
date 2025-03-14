package kr.co.skcc.oss.bap.com.job.reader;

import jakarta.persistence.EntityManagerFactory;
import kr.co.skcc.oss.com.account.domain.account.Account;
import org.springframework.batch.item.database.JpaPagingItemReader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AccountAddRoleDeptChgItemReader extends JpaPagingItemReader<Account> {

    private final static int CHUNK_SIZE = 5;

    public AccountAddRoleDeptChgItemReader(EntityManagerFactory entityManagerFactory) {

        String query = "SELECT t.empno, t.deptcd, t.reofoCd, t.vctnCd \n" +
                "  FROM UserBasic t \n" +
                " INNER JOIN Account e ON e.userid = t.empno \n" +
                "                     AND t.vctnCd <> e.vctnCd  \n" +
                "                     AND e.userid NOT LIKE '%A' \n" +
                "  LEFT OUTER JOIN Dept d ON d.deptcd = t.deptcd \n" +
                " WHERE e.useridStsCd <> 'D' \n" +
                "   AND (d.deptNm LIKE '%지사' OR d.deptNm LIKE '%영업소') \n" +
                "   AND t.reofoCd IN :reofoCd \n" +
                "   AND t.vctnCd IN :vctnCd \n";
                //TODO 아래는 지워야 함.
//                "   AND t.deptcd NOT LIKE '17%' \n" +
//                "   AND e.deptcd NOT LIKE '17%' \n";
        this.setQueryString(query);
        this.setPageSize(CHUNK_SIZE);
        this.setEntityManagerFactory(entityManagerFactory);
        this.setName("AccountStsDeptChgItemReader");
        Map<String, Object> map = new HashMap<>();

        List<String> reofoCd = Arrays.asList("250","300");
        List<String> vctnCd = Arrays.asList("340","410", "430","510","511","512");
        map.put("reofoCd",reofoCd);
        map.put("vctnCd",vctnCd);
        this.setParameterValues(map);
    }
}
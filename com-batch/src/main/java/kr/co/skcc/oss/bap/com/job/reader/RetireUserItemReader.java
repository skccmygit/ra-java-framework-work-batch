package kr.co.skcc.oss.bap.com.job.reader;


import jakarta.persistence.EntityManagerFactory;
import kr.co.skcc.oss.com.account.domain.account.Account;
import org.springframework.batch.item.database.JpaPagingItemReader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RetireUserItemReader extends JpaPagingItemReader<Account> {

    private final static int CHUNK_SIZE = 5;

    @Override
    public int getPage() {
        return 0;
    }

    public RetireUserItemReader(EntityManagerFactory entityManagerFactory) {

        this.setEntityManagerFactory(entityManagerFactory);
        this.setPageSize(CHUNK_SIZE);

        //사용자ID상태코드
        //  W:승인대기          Waiting
        //  O:정상             On progress
        //  R:반려             Reject
        //  L:잠김             Lock
        //  D:삭제             Delete
        //  T:임시비밀번호 부여  Temp password
        String query = "SELECT t FROM Account t " +
                " WHERE (t.userIdentNo NOT IN (SELECT k.empno FROM UserBasic k)" +
                "    OR t.userIdentNo IN (SELECT l.empno FROM UserBasic l WHERE l.resgDt > '' and l.resgDt < :resgDt)) " +
                "   AND t.useridStsCd IN :status " +
                "   AND t.innerUserClCd != '1' ";
        this.setQueryString(query);
        Map<String, Object> map = new HashMap<>();

        List<String> status = Arrays.asList("W","O","L","T");
        String regsDt=LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        map.put("status",status);
        map.put("resgDt",regsDt);

        this.setParameterValues(map);
    }
}
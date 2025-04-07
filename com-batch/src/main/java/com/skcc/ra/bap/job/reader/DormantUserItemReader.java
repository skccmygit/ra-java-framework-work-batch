package com.skcc.ra.bap.job.reader;


import jakarta.persistence.EntityManagerFactory;
import com.skcc.ra.account.domain.account.Account;
import org.springframework.batch.item.database.JpaPagingItemReader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DormantUserItemReader extends JpaPagingItemReader<Account> {

    private final static int CHUNK_SIZE = 5;

    @Override
    public int getPage() { return 0; }

    public DormantUserItemReader(EntityManagerFactory entityManagerFactory) {

        this.setEntityManagerFactory(entityManagerFactory);
        this.setPageSize(CHUNK_SIZE);

        //사용자ID상태코드
        //  W:승인대기          Waiting
        //  O:정상             On progress
        //  R:반려             Reject
        //  L:잠김             Lock
        //  D:삭제             Delete
        //  T:임시비밀번호 부여/비밀번호 변경 필요 Temp password
        String query =  "SELECT t FROM Account t " +
                " WHERE userid NOT IN (SELECT DISTINCT k.userid FROM UserActvyLog k " +
                "                   WHERE k.userActvyTypeCd = '1' " +
                "                     AND k.userActvyDtm >= :userActvyDtm) " +
                "   AND t.useridStsCd IN :status " +
                "   AND t.lastChngDtmd <= STR_TO_DATE(:userActvyDtm,'%Y%m%d') ";
        this.setQueryString(query);

        Map<String, Object> map = new HashMap<>();
        String userActvyDtm = LocalDate.now().minusMonths(2).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        List<String> status = Arrays.asList("O","T");

        map.put("status",status);
        map.put("userActvyDtm", userActvyDtm);
        this.setParameterValues(map);
    }
}
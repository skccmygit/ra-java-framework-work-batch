package com.skcc.ra.bap.job.reader;


import jakarta.persistence.EntityManagerFactory;
import com.skcc.ra.account.domain.hist.UserActvyLog;
import org.springframework.batch.item.database.JpaPagingItemReader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


public class MenuStatisticsItemReader extends JpaPagingItemReader<UserActvyLog> {

    private final static int CHUNK_SIZE = 5;

    public MenuStatisticsItemReader(EntityManagerFactory entityManagerFactory) {

        /*
        * UserActvyLog에 menuId는 없음
        * ScrenId 받아와서 메뉴기본 테이블과 Join 해서 MenuId 가져와야 함
        */

        String query = "SELECT DATE_FORMAT(t.userActvyDtm, '%Y%m%d') AS sumrDt, k.menuId AS menuId , COUNT(t.screnId) AS connQty " +
                       "  FROM UserActvyLog t " +
                       "  LEFT OUTER JOIN Menu k ON t.screnId = k.screnId " +
                       " WHERE t.userActvyDtm LIKE CONCAT(:date,'%') " +
                       "   AND k.menuId IS NOT NULL " +
                       " GROUP BY DATE_FORMAT(t.userActvyDtm, '%Y%m%d'), k.menuId";
        this.setQueryString(query);
        this.setPageSize(CHUNK_SIZE);
        this.setEntityManagerFactory(entityManagerFactory);
        this.setName("menuStatisticsItemReader");
        Map<String, Object> map = new HashMap<>();
        String date = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        map.put("date", date);
        this.setParameterValues(map);
    }
}
package com.skcc.ra.bap.job.reader;


import jakarta.persistence.EntityManagerFactory;
import com.skcc.ra.common.domain.userBasic.UserBasic;
import org.springframework.batch.item.database.JpaPagingItemReader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


public class DeleteUserBasicItemReader extends JpaPagingItemReader<UserBasic> {

    private final static int CHUNK_SIZE = 500;

    @Override
    public int getPage() {
        return 0;
    }

    public DeleteUserBasicItemReader(EntityManagerFactory entityManagerFactory) {

        String query = "SELECT t FROM UserBasic t Where DATE_FORMAT(t.lastChngDtmd, '%Y%m%d') < :resgDt ";
        this.setQueryString(query);
        this.setPageSize(CHUNK_SIZE);
        this.setEntityManagerFactory(entityManagerFactory);
        this.setName("DeleteUserBasicItemReader");

        Map<String, Object> map = new HashMap<>();
        String regsDt= LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        map.put("resgDt",regsDt);
        this.setParameterValues(map);
    }
}